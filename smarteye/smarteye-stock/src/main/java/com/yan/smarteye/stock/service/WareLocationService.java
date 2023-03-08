package com.yan.smarteye.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.to.WareStockRespTo;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.stock.entity.WareLocationEntity;
import com.yan.smarteye.stock.vo.ListEchartsVo;
import com.yan.smarteye.stock.vo.SonWareListVo;

import java.util.List;
import java.util.Map;

/**
 * 库存三级分区

 */
public interface WareLocationService extends IService<WareLocationEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //库存分区，三级目录
    List<WareLocationEntity> listWithTree();
    //根据catelogId调查出 路径
//    找到一个三级目录对应的完整路径 [2,25,225]  /电器/好电器/手机
    Long[] findCatelogPath(Long wlId);
    //根据wlid、wlpid、wlppid查并设置对应名称
    List<WareStockRespTo> transform(List<WareStockRespTo> tos);
    /**
     * 库存分区echarts
     */
    List<ListEchartsVo> listEcharts();
    //根据父id查询，其所有子区
    List<SonWareListVo> getSon(Long wlpId);
}

