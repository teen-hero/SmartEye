package com.yan.smarteye.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.stock.entity.OutbillEntity;
import com.yan.smarteye.stock.entity.OutbilldetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 出库单详情
 */
public interface OutbilldetailService extends IService<OutbilldetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 查询outbillid下的所有出库详情
     */
    List<OutbilldetailEntity> getByOutbillId(Long outbillId);

}

