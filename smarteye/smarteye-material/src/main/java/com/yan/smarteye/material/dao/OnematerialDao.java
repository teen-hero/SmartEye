package com.yan.smarteye.material.dao;

import com.yan.smarteye.material.entity.OnematerialEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 待上架或已上架的一种货物
 *
 */
@Mapper
public interface OnematerialDao extends BaseMapper<OnematerialEntity> {

}
