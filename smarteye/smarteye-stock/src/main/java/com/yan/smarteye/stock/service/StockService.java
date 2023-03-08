package com.yan.smarteye.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.stock.entity.StockEntity;
import com.yan.smarteye.stock.vo.saveVo.StockVo;

import java.util.Map;

/**
 * 待上架或已上架的一批货物

 */
public interface StockService extends IService<StockEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //保存stock及onestock,入库操作
    void saveInfo(StockVo stockVo);
    //检索查询
    PageUtils queryPageByCondition(Map<String, Object> params);
    //上架stock ,返回0代表onestock没全部上架stock上架失败,1代表stock上架成功
    int up(Long stockId);
    //今天新增入库单数量
    int queryTodaybill();
    //待上架的入库单元数量
    int queryTodobill();
}

