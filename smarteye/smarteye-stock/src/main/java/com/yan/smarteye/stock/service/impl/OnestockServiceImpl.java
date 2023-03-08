package com.yan.smarteye.stock.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yan.common.constant.RabbitMQConstant;
import com.yan.common.constant.RedisConstant;
import com.yan.common.to.WareStockTo;
import com.yan.common.utils.R;
import com.yan.smarteye.stock.cache.OneStockCacheHash;
import com.yan.smarteye.stock.dao.StockTypeDao;
import com.yan.smarteye.stock.entity.StockTypeEntity;
import com.yan.smarteye.stock.feign.WareStockFeignService;
import com.yan.smarteye.stock.service.StockService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.stock.dao.OnestockDao;
import com.yan.smarteye.stock.entity.OnestockEntity;
import com.yan.smarteye.stock.service.OnestockService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("onestockService")
public class OnestockServiceImpl extends ServiceImpl<OnestockDao, OnestockEntity> implements OnestockService {
    @Autowired
    StockTypeDao stockTypeDao;
    @Autowired
    WareStockFeignService wareStockFeignService;
    @Autowired
    OnestockDao onestockDao;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StockService stockService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OnestockEntity> page = this.page(
                new Query<OnestockEntity>().getPage(params),
                new QueryWrapper<OnestockEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * onestock 区间模糊查询 检索
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        String key = (String) params.get("key");
        if(key==""){ //空串设为null
            key = null;
        }
        String wlId = (String) params.get("wlId");
        if(wlId==""){ //空串设为null
            wlId = null;
        }
        String supplierId = (String) params.get("supplierId");
        if(supplierId==""){ //空串设为null
            supplierId = null;
        }
        String min = (String) params.get("min");
        if(min==""){ //空串设为null
            min = null;
        }
        String max = (String) params.get("max");
        if(max==""){ //空串设为null
            max = null;
        }
        String pagetemp = (String) params.get("page");
        Integer page = Integer.valueOf(pagetemp);
        if(StringUtils.isEmpty(pagetemp)){
            page = null;
        }
        String limitTemp = (String) params.get("limit");
        Integer limit = Integer.valueOf(limitTemp);
        if(StringUtils.isEmpty(limitTemp)){
            limit = null;
        }
        if(page==null || limit==null){ //保证page、limit不会单独出现
            page = null;
            limit = null;
        }
        //热点查询数据：key、wlid同时在缓存hash中，且min、max、page、limit都为固定值null、null、1、10
        //先看看是不是热点查询数据，是则先去缓存中拿
        if(OneStockCacheHash.hash2.containsKey(key) && OneStockCacheHash.hash3.containsKey(wlId)
            && min==null && max==null && page.equals(1) && limit.equals(10)){
            String redisKey = key+wlId;
            Object cache = redisTemplate.opsForValue().get(redisKey);
            if(cache!=null){
                return (PageUtils) cache;
            }
        }

        //查询（mp动态sql）
        List<OnestockEntity> data = onestockDao.queryPageByCondition(key,wlId,supplierId,min,max,page,limit);
        //显示真实保质期
        for (OnestockEntity onestockEntity:data){
            if (onestockEntity.getQualityPeriod()!=Integer.MAX_VALUE) { //排除永不过期的产品
                //此日期表示自1970年1月1日00:00:00 GMT以来的毫秒数
                long creat = onestockEntity.getCreateTime().getTime();
                long today = new Date().getTime();
                //转为毫秒再转为天数，不足一天的按0处置
                int time = (int) ((today / 1000) / (24 * 3600) - (creat / 1000) / (24 * 3600));
                onestockEntity.setQualityPeriod(onestockEntity.getQualityPeriod() - time);
            }
        }
        //查询总记录数
        int totalCount = onestockDao.queryConditiontoatalCount(key,wlId,supplierId,min,max);
        //封装返回数据
        PageUtils pageUtils = new PageUtils();
        if(page==null){ //未传入分页参数，默认返回第一页
            page = 1;
        }
        pageUtils.setCurrPage(page);
        pageUtils.setList(data);
        pageUtils.setTotalCount(totalCount);
        //符合热点查询的存入缓存,并设置过期时间
        if(OneStockCacheHash.hash2.containsKey(key) && OneStockCacheHash.hash3.containsKey(wlId)
                && min==null && max==null && page.equals(1) && limit.equals(10)){
            String redisKey = key+wlId;
            redisTemplate.opsForValue().set(redisKey,pageUtils,5,TimeUnit.MINUTES);
        }
        return  pageUtils;

    }

    /**
     * onestock 区间模糊查询 检索(废弃，改用自己写的mapper方法) (已废弃)
     */

    public PageUtils notusequeryPageByCondition(Map<String, Object> params) {

        QueryWrapper<OnestockEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and(w -> w.eq("onestock_id", key).or().like("value_select", key));
        }
        // 三级id没选择不应该拼这个条件  没选应该查询所有
        String wlId = (String) params.get("wlId");
        if(!StringUtils.isEmpty(wlId) && !"0".equalsIgnoreCase(wlId)){
            wrapper.eq("wl_id", wlId);
        }
        String supplierId = (String) params.get("supplierId");
        if(!StringUtils.isEmpty(supplierId) && !"0".equalsIgnoreCase(supplierId)){
            wrapper.eq("supplier_id", supplierId);
        }
        Integer min = (Integer) params.get("min");
        if(min!=null){
            // gt : 大于;  ge: 大于等于
            //TODO 真实保质期 = quality_period - （new date - creatdate） > min
            //quality_period > min + new date - creadata
            wrapper.ge("quality_period", min);
        }
        Integer max = (Integer) params.get("max");
        if(max!=null){
            try {
                //BigDecimal bigDecimal = new BigDecimal(max);
                if(max<=min){  //最大值比最小值小就没必要再检索了
                    // le: 小于等于
                    wrapper.le("quality_period", max);
                }
            } catch (Exception e) {
                System.out.println("OnestockServiceImpl：前端传来非数字字符");
            }
        }

        IPage<OnestockEntity> page = this.page(
                new Query<OnestockEntity>().getPage(params),
                wrapper
        );
        //显示真实保质期
        for (OnestockEntity onestockEntity:page.getRecords()){
            if (onestockEntity.getQualityPeriod()!=Integer.MAX_VALUE) { //排除永不过期的产品
                //此日期表示自1970年1月1日00:00:00 GMT以来的毫秒数
                long creat = onestockEntity.getCreateTime().getTime();
                long today = new Date().getTime();
                //转为毫秒再转为天数，不足一天的按0处置
                int time = (int) ((today / 1000) / (24 * 3600) - (creat / 1000) / (24 * 3600));
                onestockEntity.setQualityPeriod(onestockEntity.getQualityPeriod() - time);
            }
        }
        return new PageUtils(page);
    }

    //上架onestock，并向wms中添加warestock库存，并检测所属stock包含的onestock是不是全部上架完成，是则修改stock状态
    @Transactional
    @Override
    public void up(Long onestockId) {
        //1.修改onestock状态
        OnestockEntity onestockEntity = baseMapper.selectById(onestockId);
        onestockEntity.setPublishStatus(1);

        //2.设置WareStockTo
        WareStockTo wareStockTo = new WareStockTo();
        BeanUtils.copyProperties(onestockEntity,wareStockTo);
        StockTypeEntity stockTypeEntity = stockTypeDao.selectById(onestockEntity.getStockTypeId());
        wareStockTo.setStockTypeName(stockTypeEntity.getStockTypeName());
        //3.远程调用，向wms中添加warestock库存,添加真实库存
        //使用异步线程减少方法执行时间，因为涉及事务控制，用callable实现线程感知异常和返回结果
        //fegin远程调用丢失上下文,复制一份上下文给新的线程
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        //向线程池提交任务 有返回值
        Future<R> result = threadPoolExecutor.submit(new Callable<R>() {
            @Override
            public R call() throws Exception {
                //复制一份上下文给新的线程
                RequestContextHolder.setRequestAttributes(requestAttributes);
                R r = wareStockFeignService.add(wareStockTo);
                return r;
            }
        });
        //返回任务的执行结果
        R r = null;
        try {
            Object o = result.get();
            r = (R)o;
        } catch (InterruptedException e) {
            //异步线程处理异常，无需处理，不影响业务逻辑，有异常则 r=null，下面的判断一样会手动抛出异常
        } catch (ExecutionException e) {
            //异步线程处理异常，无需处理，不影响业务逻辑，有异常则 r=null，下面的判断一样会手动抛出异常
        }
        //不用关闭线程池，其他线程可能正在使用线程池，线程池全程保留即可
        //threadPoolExecutor.shutdown();

        //R r = wareStockFeignService.add(wareStockTo);
        //4.检测所属stock，尝试上架stock （存入消息队列，晚上空闲执行）
        //stockService.up(onestockEntity.getStockId());

        if(r!=null && r.getCode()==0){
            //上架onestock
            baseMapper.updateById(onestockEntity);
            //发送消息到消息队列，进行整批上架确认,通过消息队列实现异步处理
            rabbitTemplate.convertAndSend(RabbitMQConstant.STOCK_EXCHANGE,RabbitMQConstant.STOCK_UPSTOCKROUTINGKEY,onestockEntity);
        }else{
            throw new RuntimeException("添加库存失败");
        }

    }

    /**
     * 查询stockid下的所有详情
     */

    //将101、102、103热点key存入缓存
    //此hash表可以单独抽取成一个单例对象注入容器，在一个统一的类中进行管理
//    Map<Integer,Integer> hash = new HashMap<>();
//        {
//            hash.put(101,0);
//            hash.put(102,0);
//            hash.put(103,0);
//        }
    @Override
    public List<OnestockEntity> getByStockId(Long stockId) {
        Map<Integer,Integer> hash = OneStockCacheHash.hash1;
        //热点key先从缓存拿
        if(hash.containsKey(stockId)){
            Object cache = redisTemplate.opsForValue().get(RedisConstant.CACHE + stockId);
            return JSON.parseObject(JSON.toJSONString(cache), new TypeReference<List<OnestockEntity>>(){});
        }
        QueryWrapper<OnestockEntity> queryWrapper = new QueryWrapper<OnestockEntity>().eq("stock_id", stockId);
        List<OnestockEntity> onestockEntities = this.baseMapper.selectList(queryWrapper);
        //热点key存入缓存、设置过期时间5分钟
        if(hash.containsKey(stockId)){
           redisTemplate.opsForValue().set(RedisConstant.CACHE + stockId,onestockEntities,5, TimeUnit.MINUTES);
        }
        return onestockEntities;
    }


}