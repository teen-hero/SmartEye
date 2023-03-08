package com.yan.smarteye.stock.dao;

import com.yan.smarteye.stock.entity.StockImagesEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存货物的图片集

 */
@Mapper
public interface StockImagesDao extends BaseMapper<StockImagesEntity> {
	
}
