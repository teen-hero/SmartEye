package com.yan.smarteye.material.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.material.entity.OnematerialEntity;

import java.util.List;
import java.util.Map;

/**
 * 待上架或已上架的一种货物

 */
public interface OnematerialService extends IService<OnematerialEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //检索查询
    PageUtils queryPageByCondition(Map<String, Object> params);
    //上架onematerial，并向wms中添加warematerial库存
    void up(Long onematerialId);
    /**
     * 查询materialid下的所有详情
     */
    List<OnematerialEntity> getByMaterialId(Long materialId);
}

