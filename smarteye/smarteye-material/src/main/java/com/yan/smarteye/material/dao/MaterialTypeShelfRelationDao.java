package com.yan.smarteye.material.dao;

import com.yan.smarteye.material.entity.MaterialTypeShelfRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存种类&货架关联
 */
@Mapper
public interface MaterialTypeShelfRelationDao extends BaseMapper<MaterialTypeShelfRelationEntity> {
    //删除relationEntity集合
    void deleteBatchRelation(@Param("entities") List<MaterialTypeShelfRelationEntity> entities);
}
