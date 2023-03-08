package com.yan.smarteye.stock.service.impl;

import com.mysql.cj.util.StringUtils;
import com.yan.smarteye.stock.cache.StockCacheHash;
import com.yan.smarteye.stock.dao.OnestockDao;
import com.yan.smarteye.stock.entity.OnestockEntity;
import com.yan.smarteye.stock.service.WareLocationService;
import com.yan.smarteye.stock.vo.saveVo.OneStock;
import com.yan.smarteye.stock.vo.saveVo.StockVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.stock.dao.StockDao;
import com.yan.smarteye.stock.entity.StockEntity;
import com.yan.smarteye.stock.service.StockService;
import org.springframework.transaction.annotation.Transactional;


@Service("stockService")
public class StockServiceImpl extends ServiceImpl<StockDao, StockEntity> implements StockService {
    @Autowired
    OnestockDao onestockDao;
    @Autowired
    WareLocationService wareLocationService;
    @Autowired
    StockDao stockDao;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StockEntity> page = this.page(
                new Query<StockEntity>().getPage(params),
                new QueryWrapper<StockEntity>()
        );

        return new PageUtils(page);
    }

    //保存stock及onestock,入库操作
    // TODO（幂等） 暂时没加，偷懒了，幂等参考出库单，基本一致
    @Transactional
    @Override
    public void saveInfo(StockVo stockVo) {
        //1、保存stock基本信息`gms_stock`
        StockEntity stockEntity = new StockEntity();
        BeanUtils.copyProperties(stockVo,stockEntity);
        //保存stock的描述图片  以，为分割拼接字符串
        List<String> images = stockVo.getStockImageInfo();
        stockEntity.setStockImage(String.join(",",images));
        //上架状态默认设置为0
        stockEntity.setPublishStatus(0);
        //时间设置也可以通过mp在实体类标注时间字段的自动填充实现
        stockEntity.setCreateTime(new Date());
        stockEntity.setUpdateTime(new Date());
        this.save(stockEntity);
        //2.保存onestock的信息`gms_onestock`
        for (OneStock oneStock : stockVo.getOneStock()){
            OnestockEntity onestockEntity = new OnestockEntity();
            BeanUtils.copyProperties(oneStock,onestockEntity);
            //如果没设置保质期，则说明不会过期，设为最大值
            if (oneStock.getQualityPeriod()==null){
                onestockEntity.setQualityPeriod(Integer.MAX_VALUE);
            }
            //设置对应的stock的Id
            onestockEntity.setStockId(stockEntity.getStockId());
            //设置wlId、supplierId
            onestockEntity.setSupplierId(stockEntity.getSupplierId());
            onestockEntity.setWlId(stockEntity.getWlId());
            //设置wlpId、wlppId (虽然根据wlid后期都能查到wlpid、wlppid信息，但是为了减少查询数据库，就在onestock表中也保存了这些信息)
            Long[] catelogPath = wareLocationService.findCatelogPath(stockEntity.getWlId());
            onestockEntity.setWlpId(catelogPath[1]);
            onestockEntity.setWlppId(catelogPath[0]);
            //上架状态默认设置为0
            onestockEntity.setPublishStatus(0);
            //时间设置也可以通过mp在实体类标注时间字段的自动填充实现
            onestockEntity.setCreateTime(new Date());
            onestockEntity.setUpdateTime(new Date());
            onestockDao.insert(onestockEntity);
        }
    }

    //检索查询
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<StockEntity> wrapper = new QueryWrapper<>();

        String wlId = (String) params.get("wlId");
        if (!StringUtils.isNullOrEmpty(wlId) && !"0".equalsIgnoreCase(wlId)){
            wrapper.eq("wl_id", wlId);
        }

        String supplierId = (String) params.get("supplierId");
        if (!StringUtils.isNullOrEmpty(supplierId) && !"0".equalsIgnoreCase(supplierId)){
            wrapper.eq("supplier_id", supplierId);
        }

        String status = (String) params.get("status");
        if (!StringUtils.isNullOrEmpty(status)){
            wrapper.eq("publish_status", status);
        }

        String key = (String) params.get("key");
        if (!StringUtils.isNullOrEmpty(key)){
            wrapper.and((w) -> {
                w.eq("stock_id", key).or().like("stock_name", key);
            });
        }

        //热点查询数据：key、wlid同时在缓存hash中，且status、supplirid、page、limit都为固定值null、null、1、10
        //先看看是不是热点查询数据，是则先去缓存中拿
        if(StockCacheHash.hash2.containsKey(key) && StockCacheHash.hash3.containsKey(wlId)
                && status==null && supplierId==null && params.get("page").equals(1) && params.get("limit").equals(10)){
            String redisKey = key+wlId;
            Object cache = redisTemplate.opsForValue().get(redisKey);
            if(cache!=null){
                return (PageUtils) cache;
            }
        }

        IPage<StockEntity> page = this.page(
                new Query<StockEntity>().getPage(params),
                wrapper
        );
        //符合热点查询的存入缓存,并设置过期时间
        if(StockCacheHash.hash2.containsKey(key) && StockCacheHash.hash3.containsKey(wlId)
                && status==null && supplierId==null && params.get("page").equals(1) && params.get("limit").equals(10)){
            String redisKey = key+wlId;
            redisTemplate.opsForValue().set(redisKey,new PageUtils(page),5, TimeUnit.MINUTES);
        }

        return new PageUtils(page);
    }

    //上架stock，返回0代表onestock没全部上架stock上架失败,1代表stock上架成功
    @Override
    public int up(Long stockId) {
        //先判断其包含的onestock是否全部上架，全部上架才可进行stock上架
        List<OnestockEntity> onestockEntities = onestockDao.selectList(new QueryWrapper<OnestockEntity>().eq("stock_id", stockId));
        for (OnestockEntity onestockEntity:onestockEntities){
            if (onestockEntity.getPublishStatus()==0){  //只要onestock存在未上架的，就返回0
                return 0; //上架stock失败
            }
        }
        //上架stock
        StockEntity stockEntity = baseMapper.selectById(stockId);
        stockEntity.setPublishStatus(1);
        baseMapper.updateById(stockEntity);
        return 1;  //上架stock成功
    }

    //今天新增入库单数量
    @Cacheable(value = {"stock"},key = "#root.method.name")
    @Override
    public int queryTodaybill() {
        int res = stockDao.queryTodaybill();
        return res;
    }

    //待上架的入库单数量
    @Cacheable(value = {"stock"},key = "#root.method.name")
    @Override
    public int queryTodobill() {
        int res = stockDao.queryTodobill();
        return res;
    }

}