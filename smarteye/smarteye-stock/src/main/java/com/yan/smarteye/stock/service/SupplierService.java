package com.yan.smarteye.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.stock.entity.SupplierEntity;

import java.util.Map;

/**
 * 供用商

 */
public interface SupplierService extends IService<SupplierEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

