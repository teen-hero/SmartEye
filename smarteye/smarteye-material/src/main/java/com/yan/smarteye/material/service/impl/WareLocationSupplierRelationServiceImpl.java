package com.yan.smarteye.material.service.impl;

import com.yan.smarteye.material.entity.SupplierEntity;
import com.yan.smarteye.material.entity.WareLocationEntity;
import com.yan.smarteye.material.service.SupplierService;
import com.yan.smarteye.material.service.WareLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.material.dao.WareLocationSupplierRelationDao;
import com.yan.smarteye.material.entity.WareLocationSupplierRelationEntity;
import com.yan.smarteye.material.service.WareLocationSupplierRelationService;


@Service("wareLocationSupplierRelationService")
public class WareLocationSupplierRelationServiceImpl extends ServiceImpl<WareLocationSupplierRelationDao, WareLocationSupplierRelationEntity> implements WareLocationSupplierRelationService {

    @Autowired
    SupplierService supplierService;
    @Autowired
    WareLocationService wareLocationService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareLocationSupplierRelationEntity> page = this.page(
                new Query<WareLocationSupplierRelationEntity>().getPage(params),
                new QueryWrapper<WareLocationSupplierRelationEntity>()
        );

        return new PageUtils(page);
    }

    //发来的请求只有id，没有name参数，我们联表查询出来补全name再保存
    @Override
    public void saveDetail(WareLocationSupplierRelationEntity wareLocationSupplierRelationEntity) {
        Long supplierId = wareLocationSupplierRelationEntity.getSupplierId();
        Long wlId = wareLocationSupplierRelationEntity.getWlId();
        //1、查询详细名字
        SupplierEntity supplierEntity = supplierService.getById(supplierId);
        WareLocationEntity wareLocationEntity = wareLocationService.getById(wlId);

        wareLocationSupplierRelationEntity.setSupplierName(supplierEntity.getName());
        wareLocationSupplierRelationEntity.setWlName(wareLocationEntity.getName());

        this.save(wareLocationSupplierRelationEntity);
    }

    //据一个分类id查询和他有关的所有供用商
    @Override
    public List<SupplierEntity> getSupplierByWlId(Long wlId) {
        //1 首先根据分类catId查出CategoryBrandRelation表中所有关联数据
        List<WareLocationSupplierRelationEntity> relationEntities = baseMapper.selectList(new QueryWrapper<WareLocationSupplierRelationEntity>().eq("wl_id", wlId));
        //2 根据CategoryBrandRelationEntity 的 List 查出 brand list
        List<SupplierEntity> collect = relationEntities.stream().map(item -> {
            //2.1 对于每一个CategoryBrandRelationEntity 先拿出其BrandId
            Long supplierId = item.getSupplierId();
            //2.2 用其BrandId 调用 brandService拿到brand实体类
            SupplierEntity supplierEntity = supplierService.getById(supplierId);
            return supplierEntity;
        }).collect(Collectors.toList());

        //3 最终返回结果
        return collect;
    }

}