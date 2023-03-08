package com.yan.smarteye.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yan.common.constant.MaterialConstant;
import com.yan.smarteye.material.dao.ShelfDao;
import com.yan.smarteye.material.dao.MaterialTypeShelfRelationDao;
import com.yan.smarteye.material.dao.WareLocationDao;
import com.yan.smarteye.material.entity.ShelfEntity;
import com.yan.smarteye.material.entity.MaterialTypeShelfRelationEntity;
import com.yan.smarteye.material.entity.WareLocationEntity;
import com.yan.smarteye.material.service.WareLocationService;
import com.yan.smarteye.material.vo.ShelfRelationVo;
import com.yan.smarteye.material.vo.MaterialTypeRespVo;
import com.yan.smarteye.material.vo.MaterialTypeShelfVo;
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

import com.yan.smarteye.material.dao.MaterialTypeDao;
import com.yan.smarteye.material.entity.MaterialTypeEntity;
import com.yan.smarteye.material.service.MaterialTypeService;
import org.springframework.transaction.annotation.Transactional;


@Service("materialTypeService")
public class MaterialTypeServiceImpl extends ServiceImpl<MaterialTypeDao, MaterialTypeEntity> implements MaterialTypeService {

    @Autowired
    MaterialTypeShelfRelationDao materialTypeShelfRelationDao;
    @Autowired
    ShelfDao shelfDao;
    @Autowired
    WareLocationDao wareLocationDao;
    @Autowired
    WareLocationService wareLocationService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MaterialTypeEntity> page = this.page(
                new Query<MaterialTypeEntity>().getPage(params),
                new QueryWrapper<MaterialTypeEntity>()
        );

        return new PageUtils(page);
    }

    //查询某三级目录分类目录下的库存种类，0为全部
    @Override
    public PageUtils queryBaseMaterialTypePage(Map<String, Object> params, Long wlId, String type) {
        //分基本货物、货架外的货物的情况进行查询
        QueryWrapper<MaterialTypeEntity> queryWrapper = new QueryWrapper<MaterialTypeEntity>().eq("material_type_type","base".equalsIgnoreCase(type)? MaterialConstant.MaterialTypeEnum.STOCK_TYPE_TYPE_BASE.getCode(): MaterialConstant.MaterialTypeEnum.STOCK_TYPE_TYPE_OUT.getCode());

        //如果传入的catelogId不为0 我们才查询catelogId
        if(wlId != 0){
            queryWrapper.eq("wl_id",wlId);
        }

        //得到params参数列表中的Key 如果不为空 进行规则匹配(返回属性id等于Key或者是属性name like key)
        String key = (String) params.get("key");
        if(key!=null){
            //attr_id  attr_name
            queryWrapper.and((wrapper)->{
                wrapper.eq("material_type_id",key).or().like("material_type_name",key);
            });
        }

        //调用mybatis-plus serviceImpl层的page方法返回Ipage对象
        // 其参数是根据Query工具类生成的Page对象 和 queryWrapper
        IPage<MaterialTypeEntity> page = this.page(
                new Query<MaterialTypeEntity>().getPage(params),
                queryWrapper
        );

        //5 把生成的Ipage对象封装到pageUtils里
        PageUtils pageUtils = new PageUtils(page);

        //6 从Ipage得到记录
        List<MaterialTypeEntity> records = page.getRecords();
        List<MaterialTypeRespVo> respVos = records.stream().map((materialTypeEntity) -> {
            // 生成一个AttrRespVo的VO对象
            MaterialTypeRespVo attrRespVo = new MaterialTypeRespVo();
            // 先把原来的数据copy到其中
            BeanUtils.copyProperties(materialTypeEntity, attrRespVo);

            //设置分组的名字
            if("base".equalsIgnoreCase(type)) {
                MaterialTypeShelfRelationEntity materialTypeShelfRelationEntity = materialTypeShelfRelationDao.selectOne(new QueryWrapper<MaterialTypeShelfRelationEntity>().eq("material_type_id", materialTypeEntity.getMaterialTypeId()));
                if (materialTypeShelfRelationEntity != null && materialTypeShelfRelationEntity.getShelfId() != null) {
                    ShelfEntity shelfEntity = shelfDao.selectById(materialTypeShelfRelationEntity.getShelfId());
                    attrRespVo.setShelfName(shelfEntity.getShelfName());
                }
            }
            //设置分类的名字
            WareLocationEntity wareLocationEntity = wareLocationDao.selectById(materialTypeEntity.getWlId());
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
    public void saveDetial(MaterialTypeShelfVo materialTypeShelfVo) {
        //1.保存materialTypeEntity库存种类信息
        MaterialTypeEntity materialTypeEntity = new MaterialTypeEntity();
        BeanUtils.copyProperties(materialTypeShelfVo,materialTypeEntity);
        save(materialTypeEntity);
        //2.保存库存与货架的关系
        //如果是基本货物（基本货物才有货架关系）
        if(materialTypeEntity.getMaterialTypeType() == MaterialConstant.MaterialTypeEnum.STOCK_TYPE_TYPE_BASE.getCode()) {
            //如果没绑定货架id，则不添加关联关系
            if(materialTypeEntity.getMaterialTypeId()!=null){
                MaterialTypeShelfRelationEntity materialTypeShelfRelationEntity = new MaterialTypeShelfRelationEntity();
                materialTypeShelfRelationEntity.setShelfId(materialTypeShelfVo.getShelfId());
                materialTypeShelfRelationEntity.setMaterialTypeId(materialTypeEntity.getMaterialTypeId());
                materialTypeShelfRelationDao.insert(materialTypeShelfRelationEntity);
            }
        }
    }
    //根据id查询，回显
    @Override
    public MaterialTypeRespVo getMaterialTypeInfo(Long materialTypeId) {
        //获取MaterialTypeEntity
        MaterialTypeEntity materialTypeEntity = getById(materialTypeId);
        MaterialTypeRespVo materialTypeRespVo = new MaterialTypeRespVo();
        //将attrEntity属性值复制给 attrRespVo
        BeanUtils.copyProperties(materialTypeEntity,materialTypeRespVo);
        //给 attrRespVo设置groupName、catelogPath属性
        //根据catelogId查出 路径 并设置
        Long[] wlPath = wareLocationService.findCatelogPath(materialTypeEntity.getWlId());
        materialTypeRespVo.setWarelocationPath(wlPath);
        //如果是基本属性（基本属性才有分组信息）
        if(materialTypeEntity.getMaterialTypeType() == MaterialConstant.MaterialTypeEnum.STOCK_TYPE_TYPE_BASE.getCode()) {
            //查groupName  并设置
            MaterialTypeShelfRelationEntity relationEntity = materialTypeShelfRelationDao.selectOne(new QueryWrapper<MaterialTypeShelfRelationEntity>().eq("material_type_id", materialTypeId));

            if (relationEntity != null) {
                ShelfEntity shelfEntity = shelfDao.selectOne(new QueryWrapper<ShelfEntity>().eq("shelf_id", relationEntity.getShelfId()));
                if (shelfEntity != null) {
                    materialTypeRespVo.setShelfName(shelfEntity.getShelfName());
                }
            }
        }
        return materialTypeRespVo;
    }

    @Transactional
    @Override
    public void updateDetial(MaterialTypeShelfVo materialTypeShelfVo) {
        //1 接受前端传来的Vo
        MaterialTypeEntity materialTypeEntity = new MaterialTypeEntity();
        //2 copy
        BeanUtils.copyProperties(materialTypeShelfVo,materialTypeEntity);
        //3 先更新attrEntity
        this.updateById(materialTypeEntity);
        //4 如果是基本属性再修改(基本属性才有分组信息)
        if(materialTypeEntity.getMaterialTypeType() == MaterialConstant.MaterialTypeEnum.STOCK_TYPE_TYPE_BASE.getCode()) {
            //1、修改分组关联
            MaterialTypeShelfRelationEntity relationEntity = new MaterialTypeShelfRelationEntity();
            relationEntity.setShelfId(materialTypeShelfVo.getShelfId());
            relationEntity.setMaterialTypeId(materialTypeShelfVo.getMaterialTypeId());

            //判断数据库中是否有这个relationEntity 有了更新 没有添加
            Integer count = materialTypeShelfRelationDao.selectCount(new QueryWrapper<MaterialTypeShelfRelationEntity>().eq("material_type_id", materialTypeEntity.getMaterialTypeId()));
            if (count > 0) { //说明这个属性不为空，设置过。 才可以正常修改，因为修改时要先查到这个，如果没有就不能修改，而是插入

                materialTypeShelfRelationDao.update(relationEntity, new UpdateWrapper<MaterialTypeShelfRelationEntity>().eq("material_type_id", materialTypeEntity.getMaterialTypeId()));

            } else {     //没有就不修改，直接是插入新增
                materialTypeShelfRelationDao.insert(relationEntity);
            }
        }
    }

    //获取货架关联的所有库存种类
    @Override
    public List<MaterialTypeEntity> getRelationMaterialType(Long shelfId) {
        //1 在realation表中查出所有与attrgroupId相关的realation实体类
        List<MaterialTypeShelfRelationEntity> entities = materialTypeShelfRelationDao.selectList(new QueryWrapper<MaterialTypeShelfRelationEntity>().eq("shelf_id", shelfId));

        //2 拿出这些实体类对应的 属性ids
        List<Long> materialTypeIds = entities.stream().map((entity) -> {
            return entity.getMaterialTypeId();
        }).collect(Collectors.toList());
        //非空判断
        if(materialTypeIds == null || materialTypeIds.size() == 0){
            return null;
        }
        //3 返回查询的数据
        Collection<MaterialTypeEntity> materialTypeEntities = this.listByIds(materialTypeIds);
        return (List<MaterialTypeEntity>) materialTypeEntities;
    }

    //获取属性分组没有关联的其他属性
    @Override
    public PageUtils getNoRelationMaterialType(Map<String, Object> params, Long shelfId) {
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
        List<MaterialTypeShelfRelationEntity> groupId = materialTypeShelfRelationDao.selectList(new QueryWrapper<MaterialTypeShelfRelationEntity>().in("shelf_id", collect));
        //获取attrIds
        List<Long> materialTypeIds = groupId.stream().map(item -> {
            return item.getMaterialTypeId();
        }).collect(Collectors.toList());

        //2.3)、从当前分类的所有属性中移除这些属性；
        //当前分类下的 基本属性
        QueryWrapper<MaterialTypeEntity> wrapper = new QueryWrapper<MaterialTypeEntity>().eq("wl_id", wlId).eq("material_type_type",MaterialConstant.MaterialTypeEnum.STOCK_TYPE_TYPE_BASE.getCode());
        if(materialTypeIds!=null && materialTypeIds.size()>0){
            wrapper.notIn("material_type_id", materialTypeIds);
        }
        String key = (String) params.get("key");
        if(key!=null){
            wrapper.and((w)->{
                w.eq("material_type_id",key).or().like("material_type_name",key);
            });
        }
        IPage<MaterialTypeEntity> page = this.page(new Query<MaterialTypeEntity>().getPage(params), wrapper);

        PageUtils pageUtils = new PageUtils(page);

        return pageUtils;
    }
    //删除库存种类与货架的关联关系
    @Override
    public void deleteRelation(ShelfRelationVo[] vos) {
        //1 把vos转为relationEntity集合
        List<MaterialTypeShelfRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            MaterialTypeShelfRelationEntity relationEntity = new MaterialTypeShelfRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        //2 删除这些relationEntity集合
        materialTypeShelfRelationDao.deleteBatchRelation(entities);
    }


}