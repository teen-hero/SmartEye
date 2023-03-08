package com.yan.smarteye.material.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.material.entity.MaterialValueEntity;

import java.util.Map;

/**
 * 货物的库存种类值

 */
public interface MaterialValueService extends IService<MaterialValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

