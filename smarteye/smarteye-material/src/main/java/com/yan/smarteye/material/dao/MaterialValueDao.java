package com.yan.smarteye.material.dao;

import com.yan.smarteye.material.entity.MaterialValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 货物的库存种类值
 */
@Mapper
public interface MaterialValueDao extends BaseMapper<MaterialValueEntity> {
	
}
