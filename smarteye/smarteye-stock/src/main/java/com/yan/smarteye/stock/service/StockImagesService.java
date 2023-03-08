package com.yan.smarteye.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.stock.entity.StockImagesEntity;

import java.util.Map;

/**
 * 库存货物的图片集

 */
public interface StockImagesService extends IService<StockImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

