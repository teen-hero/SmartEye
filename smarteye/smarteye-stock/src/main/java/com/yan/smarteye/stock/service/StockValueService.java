package com.yan.smarteye.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.stock.entity.StockValueEntity;

import java.util.Map;

/**
 * 货物的库存种类值

 */
public interface StockValueService extends IService<StockValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

