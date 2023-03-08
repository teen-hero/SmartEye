package com.yan.smarteye.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.to.WareMaterialTo;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.ware.entity.WarematerialEntity;

import java.util.Map;

/**
 * 库存品
 *
 */
public interface WarematerialService extends IService<WarematerialEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 向wms中添加warematerial库存(分库库存)
     */
    void addMaterial(WareMaterialTo wareMaterialTo);
    //条件查询
    PageUtils queryPageByCondition(Map<String, Object> params);
}

