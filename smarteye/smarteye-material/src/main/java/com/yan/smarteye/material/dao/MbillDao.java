package com.yan.smarteye.material.dao;

import com.yan.smarteye.material.entity.MbillEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yan.smarteye.material.vo.MouthMbillRespVo;
import com.yan.smarteye.material.vo.MouthMbillbingtuVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 物料申请单
 *
 */
@Mapper
public interface MbillDao extends BaseMapper<MbillEntity> {
    /**
     * 查询今天新增物料申请单数量
     */
    int queryTodayMbill();
    /**
     * 待处理的物料申请单数量
     */
    int queryTodoMbill();
    /**
     * 查询近一个月物料申请单的数量趋势，以天为分组
     */
    List<MouthMbillRespVo> queryMouthMbill();
    //查询近一个月物料申请单来自的分库的分布（饼图）
    List<MouthMbillbingtuVo> queryMouthMbillbingtu();
}
