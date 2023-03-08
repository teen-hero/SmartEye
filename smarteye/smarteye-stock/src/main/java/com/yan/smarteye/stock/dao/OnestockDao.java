package com.yan.smarteye.stock.dao;

import com.yan.smarteye.stock.entity.OnestockEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 待上架或已上架的一种货物

 */
@Mapper
public interface OnestockDao extends BaseMapper<OnestockEntity> {

    //条件查询，动态拼接sql条件
    List<OnestockEntity> queryPageByCondition(@Param("key") String key, @Param("wlId")String wlId,
                                                 @Param("supplierId")String supplierId, @Param("min")String min,
                                                 @Param("max")String max, @Param("page") Integer page, @Param("size") Integer limit);

    //查询总记录数
    int queryConditiontoatalCount(@Param("key") String key, @Param("wlId")String wlId,
                                  @Param("supplierId")String supplierId, @Param("min")String min,
                                  @Param("max")String max);
}
