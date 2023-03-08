package com.yan.smarteye.stock.dao;

import com.yan.smarteye.stock.entity.StockTypeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存种类

 */
@Mapper
public interface StockTypeDao extends BaseMapper<StockTypeEntity> {
	
}
