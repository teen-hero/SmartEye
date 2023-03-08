package com.yan.smarteye.integration.dao;

import com.yan.smarteye.integration.entity.EmployeeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工表
 */
@Mapper
public interface EmployeeDao extends BaseMapper<EmployeeEntity> {
	
}
