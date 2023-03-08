package com.yan.smarteye.stock.dao;

import com.yan.smarteye.stock.entity.ShelfEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yan.smarteye.stock.vo.UpShelfInfoResp;
import com.yan.smarteye.stock.vo.UpShelfInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 货架
 *
 */
@Mapper
public interface ShelfDao extends BaseMapper<ShelfEntity> {
    //查询wlId下的货架的上架情况（onestock）
    //有货的货架
    List<UpShelfInfoResp> queryUpshelfInfo(@Param("wlId") Long wlId);

    //查询wlId下的货架的上架情况（onestock）详情
    List<UpShelfInfoVo> queryUpshelfInfodetial(@Param("wlId")Long wlId, @Param("shelfName")String shelfName);
}
