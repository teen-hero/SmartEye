package com.yan.smarteye.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.stock.entity.OutbillEntity;
import com.yan.smarteye.stock.exception.LockStockException;
import com.yan.smarteye.stock.exception.MiTokenException;
import com.yan.smarteye.stock.vo.saveoutbillvo.OutbillVo;

import java.util.Map;

/**
 * 出库单

 */
public interface OutbillService extends IService<OutbillEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 保存出库单及出库详情
     * 新建库存单的同时立即减掉对应库存，此时库存虽然没取走，但是后续出库单无法再出库这部分库存
     */
    void saveInfo(OutbillVo outbillVo) throws MiTokenException, LockStockException;
    //今天新增出库单数量
    int queryTodayOutbill();
    //待处理的出库单数量
    int queryTodoOutbill();
    /**
     * 查询outbillid的出库单信息
     */
    OutbillEntity getInfo(Long outbillId);
}

