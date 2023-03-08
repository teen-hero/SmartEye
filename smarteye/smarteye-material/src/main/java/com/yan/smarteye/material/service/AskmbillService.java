package com.yan.smarteye.material.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.material.entity.AskmbillEntity;

import java.util.Map;

/**
 * 物料申请单详情
 *
 */
public interface AskmbillService extends IService<AskmbillEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

