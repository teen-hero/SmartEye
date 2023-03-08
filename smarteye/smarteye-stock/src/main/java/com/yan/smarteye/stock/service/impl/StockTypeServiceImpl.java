package com.yan.smarteye.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yan.common.constant.StockConstant;
import com.yan.smarteye.stock.dao.ShelfDao;
import com.yan.smarteye.stock.dao.StockTypeShelfRelationDao;
import com.yan.smarteye.stock.dao.WareLocationDao;
import com.yan.smarteye.stock.entity.ShelfEntity;
import com.yan.smarteye.stock.entity.StockTypeShelfRelationEntity;
import com.yan.smarteye.stock.entity.WareLocationEntity;
import com.yan.smarteye.stock.service.WareLocationService;
import com.yan.smarteye.stock.vo.ShelfRelationVo;
import com.yan.smarteye.stock.vo.StockTypeRespVo;
import com.yan.smarteye.stock.vo.StockTypeShelfVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.stock.dao.StockTypeDao;
import com.yan.smarteye.stock.entity.StockTypeEntity;
import com.yan.smarteye.stock.service.StockTypeService;
import org.springframework.transaction.annotation.Transactional;


@Service("stockTypeService")
public class StockTypeServiceImpl extends ServiceImpl<StockTypeDao, StockTypeEntity> implements StockTypeService {

    @Autowired
    StockTypeShelfRelationDao stockTypeShelfRelationDao;
    @Autowired
    ShelfDao shelfDao;
    @Autowired
    WareLocationDao wareLocationDao;
    @Autowired
    WareLocationService wareLocationService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StockTypeEntity> page = this.page(
                new Query<StockTypeEntity>().getPage(params),
                new QueryWrapper<StockTypeEntity>()
        );

        return new PageUtils(page);
    }

    //查询某三级目录分类目录下的库存种类，0为全部
    @Override
    public PageUtils queryBaseStockTypePage(Map<String, Object> params, Long wlId, String type) {
        //分基本货物、货架外的货物的情况进行查询
        QueryWrapper<StockTypeEntity> queryWrapper = new QueryWrapper<StockTypeEntity>().eq("stock_type_type","base".equalsIgnoreCase(type)? StockConstant.StockTypeEnum.STOCK_TYPE_TYPE_BASE.getCode(): StockConstant.StockTypeEnum.STOCK_TYPE_TYPE_OUT.getCode());

        //如果传入的catelogId不为0 我们才查询catelogId
        if(wlId != 0){
            queryWrapper.eq("wl_id",wlId);
        }

        //得到params参数列表中的Key 如果不为空 进行规则匹配(返回属性id等于Key或者是属性name like key)
        String key = (String) params.get("key");
        if(key!=null){
            //attr_id  attr_name
            queryWrapper.and((wrapper)->{
                wrapper.eq("stock_type_id",key).or().like("stock_type_name",key);
            });
        }

        //调用mybatis-plus serviceImpl层的page方法返回Ipage对象
        // 其参数是根据Query工具类生成的Page对象 和 queryWrapper
        IPage<StockTypeEntity> page = this.page(
                new Query<StockTypeEntity>().getPage(params),
                queryWrapper
        );

        //5 把生成的Ipage对象封装到pageUtils里
        PageUtils pageUtils = new PageUtils(page);

        //6 从Ipage得到记录
        List<StockTypeEntity> records = page.getRecords();
        List<StockTypeRespVo> respVos = records.stream().map((stockTypeEntity) -> {
            // 生成一个AttrRespVo的VO对象
            StockTypeRespVo attrRespVo = new StockTypeRespVo();
            // 先把原来的数据copy到其中
            BeanUtils.copyProperties(stockTypeEntity, attrRespVo);

            //设置分组的名字
            if("base".equalsIgnoreCase(type)) {
                StockTypeShelfRelationEntity stockTypeShelfRelationEntity = stockTypeShelfRelationDao.selectOne(new QueryWrapper<StockTypeShelfRelationEntity>().eq("stock_type_id", stockTypeEntity.getStockTypeId()));
                if (stockTypeShelfRelationEntity != null && stockTypeShelfRelationEntity.getShelfId() != null) {
                    ShelfEntity shelfEntity = shelfDao.selectById(stockTypeShelfRelationEntity.getShelfId());
                    attrRespVo.setShelfName(shelfEntity.getShelfName());
                }
            }
            //设置分类的名字
            WareLocationEntity wareLocationEntity = wareLocationDao.selectById(stockTypeEntity.getWlId());
            if (wareLocationEntity != null) {
                attrRespVo.setWlName(wareLocationEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVos);
        return pageUtils;

    }
    //保存库存种类信息 及 对应的库存与货架关系
    @Transactional
    @Override
    public void saveDetial(StockTypeShelfVo stockTypeShelfVo) {
        //1.保存stockTypeEntity库存种类信息
        StockTypeEntity stockTypeEntity = new StockTypeEntity();
        BeanUtils.copyProperties(stockTypeShelfVo,stockTypeEntity);
        save(stockTypeEntity);
        //2.保存库存与货架的关系
        //如果是基本货物（基本货物才有货架关系）
        if(stockTypeEntity.getStockTypeType() == StockConstant.StockTypeEnum.STOCK_TYPE_TYPE_BASE.getCode()) {
            //如果没绑定货架id，则不添加关联关系
            if(stockTypeShelfVo.getShelfId()!=null){
                StockTypeShelfRelationEntity stockTypeShelfRelationEntity = new StockTypeShelfRelationEntity();
                stockTypeShelfRelationEntity.setShelfId(stockTypeShelfVo.getShelfId());
                stockTypeShelfRelationEntity.setStockTypeId(stockTypeEntity.getStockTypeId());
                stockTypeShelfRelationDao.insert(stockTypeShelfRelationEntity);
            }
        }
    }
    //根据id查询，回显
    @Override
    public StockTypeRespVo getStockTypeInfo(Long stockTypeId) {
        //获取StockTypeEntity
        StockTypeEntity stockTypeEntity = getById(stockTypeId);
        StockTypeRespVo stockTypeRespVo = new StockTypeRespVo();
        //将attrEntity属性值复制给 attrRespVo
        BeanUtils.copyProperties(stockTypeEntity,stockTypeRespVo);
        //给 attrRespVo设置groupName、catelogPath属性
        //根据catelogId查出 路径 并设置
        Long[] wlPath = wareLocationService.findCatelogPath(stockTypeEntity.getWlId());
        stockTypeRespVo.setWarelocationPath(wlPath);
        //如果是基本属性（基本属性才有分组信息）
        if(stockTypeEntity.getStockTypeType() == StockConstant.StockTypeEnum.STOCK_TYPE_TYPE_BASE.getCode()) {
            //查groupName  并设置
            StockTypeShelfRelationEntity relationEntity = stockTypeShelfRelationDao.selectOne(new QueryWrapper<StockTypeShelfRelationEntity>().eq("stock_type_id", stockTypeId));

            if (relationEntity != null) {
                ShelfEntity shelfEntity = shelfDao.selectOne(new QueryWrapper<ShelfEntity>().eq("shelf_id", relationEntity.getShelfId()));
                if (shelfEntity != null) {
                    stockTypeRespVo.setShelfName(shelfEntity.getShelfName());
                }
            }
        }
        return stockTypeRespVo;
    }

    @Transactional
    @Override
    public void updateDetial(StockTypeShelfVo stockTypeShelfVo) {
        //1 接受前端传来的Vo
        StockTypeEntity stockTypeEntity = new StockTypeEntity();
        //2 copy
        BeanUtils.copyProperties(stockTypeShelfVo,stockTypeEntity);
        //3 先更新attrEntity
        this.updateById(stockTypeEntity);
        //4 如果是基本属性再修改(基本属性才有分组信息)
        if(stockTypeEntity.getStockTypeType() == StockConstant.StockTypeEnum.STOCK_TYPE_TYPE_BASE.getCode()) {
            //1、修改分组关联
            StockTypeShelfRelationEntity relationEntity = new StockTypeShelfRelationEntity();
            relationEntity.setShelfId(stockTypeShelfVo.getShelfId());
            relationEntity.setStockTypeId(stockTypeShelfVo.getStockTypeId());

            //判断数据库中是否有这个relationEntity 有了更新 没有添加
            Integer count = stockTypeShelfRelationDao.selectCount(new QueryWrapper<StockTypeShelfRelationEntity>().eq("stock_type_id", stockTypeEntity.getStockTypeId()));
            if (count > 0) { //说明这个属性不为空，设置过。 才可以正常修改，因为修改时要先查到这个，如果没有就不能修改，而是插入

                stockTypeShelfRelationDao.update(relationEntity, new UpdateWrapper<StockTypeShelfRelationEntity>().eq("stock_type_id", stockTypeEntity.getStockTypeId()));

            } else {     //没有就不修改，直接是插入新增
                stockTypeShelfRelationDao.insert(relationEntity);
            }
        }
    }

    //获取货架关联的所有库存种类
    @Override
    public List<StockTypeEntity> getRelationStockType(Long shelfId) {
        //1 在realation表中查出所有与attrgroupId相关的realation实体类
        List<StockTypeShelfRelationEntity> entities = stockTypeShelfRelationDao.selectList(new QueryWrapper<StockTypeShelfRelationEntity>().eq("shelf_id", shelfId));

        //2 拿出这些实体类对应的 属性ids
        List<Long> stockTypeIds = entities.stream().map((entity) -> {
            return entity.getStockTypeId();
        }).collect(Collectors.toList());
        //非空判断
        if(stockTypeIds == null || stockTypeIds.size() == 0){
            return null;
        }
        //3 返回查询的数据
        Collection<StockTypeEntity> stockTypeEntities = this.listByIds(stockTypeIds);
        return (List<StockTypeEntity>) stockTypeEntities;
    }

    //获取属性分组没有关联的其他属性
    @Override
    public PageUtils getNoRelationStockType(Map<String, Object> params, Long shelfId) {
        //1、当前分组只能关联自己所属的分类里面的所有属性

        //1.1 根据attrgroupId获取attrGroupEntity
        ShelfEntity shelfEntity = shelfDao.selectById(shelfId);
        //1.2 attrGroupEntity得到catelogId
        Long wlId = shelfEntity.getWlId();


        //2、当前分组只能关联别的分组没有引用的属性
        //2.1)、当前分类下的所有分组
        List<ShelfEntity> shelfEntities = shelfDao.selectList(new QueryWrapper<ShelfEntity>().eq("wl_id", wlId));
        // 拿到所有分组的AttrGroupId
        List<Long> collect = shelfEntities.stream().map(item -> {
            return item.getShelfId();
        }).collect(Collectors.toList());

        //2.2)、这些分组关联的属性
        List<StockTypeShelfRelationEntity> groupId = stockTypeShelfRelationDao.selectList(new QueryWrapper<StockTypeShelfRelationEntity>().in("shelf_id", collect));
        //获取attrIds
        List<Long> stockTypeIds = groupId.stream().map(item -> {
            return item.getStockTypeId();
        }).collect(Collectors.toList());

        //2.3)、从当前分类的所有属性中移除这些属性；
        //当前分类下的 基本属性
        QueryWrapper<StockTypeEntity> wrapper = new QueryWrapper<StockTypeEntity>().eq("wl_id", wlId).eq("stock_type_type",StockConstant.StockTypeEnum.STOCK_TYPE_TYPE_BASE.getCode());
        if(stockTypeIds!=null && stockTypeIds.size()>0){
            wrapper.notIn("stock_type_id", stockTypeIds);
        }
        String key = (String) params.get("key");
        if(key!=null){
            wrapper.and((w)->{
                w.eq("stock_type_id",key).or().like("stock_type_name",key);
            });
        }
        IPage<StockTypeEntity> page = this.page(new Query<StockTypeEntity>().getPage(params), wrapper);

        PageUtils pageUtils = new PageUtils(page);

        return pageUtils;
    }
    //删除库存种类与货架的关联关系
    @Override
    public void deleteRelation(ShelfRelationVo[] vos) {
        //1 把vos转为relationEntity集合
        List<StockTypeShelfRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            StockTypeShelfRelationEntity relationEntity = new StockTypeShelfRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        //2 删除这些relationEntity集合
        stockTypeShelfRelationDao.deleteBatchRelation(entities);
    }


}