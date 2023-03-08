package com.yan.smarteye.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.stock.entity.ShelfEntity;
import com.yan.smarteye.stock.entity.StockTypeEntity;
import com.yan.smarteye.stock.vo.ShelfWithStockTypeVo;
import com.yan.smarteye.stock.vo.UpShelfInfoResp;
import com.yan.smarteye.stock.vo.UpShelfInfoVo;

import java.util.List;
import java.util.Map;

/**
 * 货架

 */
public interface ShelfService extends IService<ShelfEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //根据三级目录id查出其下的数据并分页查询，并支持查找（如果参数中有就查找，没有就不查），0查全部
    PageUtils queryPage(Map<String, Object> params,Long wlId);
    //根据分区id查出所有的货架以及这些货架里面的库存种类
    List<ShelfWithStockTypeVo> getShelfWithStockTypeBywlId(Long wlId);
    //查询wlId下的货架的上架情况（onestock）
    List<UpShelfInfoResp> queryUpshelf(Long wlId);
    //查询wlId下的货架的上架情况（onestock）详情
    List<UpShelfInfoVo> queryUpshelfInfo(Long wlId, String shelfName);
}

