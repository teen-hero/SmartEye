package com.yan.smarteye.stock.dao;

import com.yan.smarteye.stock.entity.StockEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 待上架或已上架的一批货物

 */
@Mapper
public interface StockDao extends BaseMapper<StockEntity> {
    //今天新增入库单数量
    int queryTodaybill();
    //待上架的入库单数量
    int queryTodobill();
}
