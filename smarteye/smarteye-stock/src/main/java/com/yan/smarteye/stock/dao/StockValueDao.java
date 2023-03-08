package com.yan.smarteye.stock.dao;

import com.yan.smarteye.stock.entity.StockValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 货物的库存种类值

 */
@Mapper
public interface StockValueDao extends BaseMapper<StockValueEntity> {
	
}
