package com.yan.smarteye.material.dao;

import com.yan.smarteye.material.entity.MaterialTypeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存种类
 */
@Mapper
public interface MaterialTypeDao extends BaseMapper<MaterialTypeEntity> {
	
}
