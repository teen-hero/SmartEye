package com.yan.smarteye.stock.dao;

import com.yan.smarteye.stock.entity.StockTypeShelfRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存种类&货架关联
 *
 */
@Mapper
public interface StockTypeShelfRelationDao extends BaseMapper<StockTypeShelfRelationEntity> {
    //删除relationEntity集合
    void deleteBatchRelation(@Param("entities") List<StockTypeShelfRelationEntity> entities);
}
