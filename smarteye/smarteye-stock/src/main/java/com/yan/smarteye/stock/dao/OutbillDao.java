package com.yan.smarteye.stock.dao;

import com.yan.smarteye.stock.entity.OutbillEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出库单
 *
 */
@Mapper
public interface OutbillDao extends BaseMapper<OutbillEntity> {
    //今天新增出库单数量
    int queryTodayOutbill();
    //待处理的出库单数量
    int queryTodoOutbill();

}
