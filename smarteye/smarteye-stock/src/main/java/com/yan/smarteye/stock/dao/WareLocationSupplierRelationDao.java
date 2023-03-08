package com.yan.smarteye.stock.dao;

import com.yan.smarteye.stock.entity.WareLocationSupplierRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存分区&供用商关联

 */
@Mapper
public interface WareLocationSupplierRelationDao extends BaseMapper<WareLocationSupplierRelationEntity> {
	
}
