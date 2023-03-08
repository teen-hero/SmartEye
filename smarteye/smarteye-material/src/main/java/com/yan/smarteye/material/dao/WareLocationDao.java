package com.yan.smarteye.material.dao;

import com.yan.smarteye.material.entity.WareLocationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存三级分区
 *
 */
@Mapper
public interface WareLocationDao extends BaseMapper<WareLocationEntity> {
	
}
