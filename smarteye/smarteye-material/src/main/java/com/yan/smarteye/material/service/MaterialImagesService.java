package com.yan.smarteye.material.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.material.entity.MaterialImagesEntity;

import java.util.Map;

/**
 * 库存货物的图片集

 */
public interface MaterialImagesService extends IService<MaterialImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

