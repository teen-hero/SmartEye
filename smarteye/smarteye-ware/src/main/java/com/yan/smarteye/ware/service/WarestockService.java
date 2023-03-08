package com.yan.smarteye.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.to.CatalogueResoTo;
import com.yan.common.to.LowWarnCountTo;
import com.yan.common.to.WareMaterialTo;
import com.yan.common.to.WareStockTo;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.ware.entity.OnestockupTaskEntity;
import com.yan.smarteye.ware.entity.OutbillTaskEntity;
import com.yan.smarteye.ware.entity.WarestockEntity;
import com.yan.smarteye.ware.exception.NoStockException;
import com.yan.smarteye.ware.exception.NullWareStock;
import com.yan.smarteye.ware.vo.CatalogueVo;
import com.yan.smarteye.ware.vo.OutbilldetailVo;

import java.util.List;
import java.util.Map;

/**
 * 库存品
 *
 */
public interface WarestockService extends IService<WarestockEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //向wms中添加warestock库存
    void add(WareStockTo wareStockTo);
    //条件查询
    PageUtils queryPageByCondition(Map<String, Object> params);
    //返回低于预警库存的货物id、valueSelect
    List<LowWarnCountTo> getLowWarnCount();
    //返回低于预警库存的货物
    PageUtils getDangerCount(Map<String, Object> params);
    /**
     * 生成出库单，立即减去库存
     */
    void billOut(List<OutbilldetailVo> vos) throws NoStockException, NullWareStock;
    /**
     * 出库可供选择目录
     */
    List<CatalogueVo> listWithGroup(int group);
    //根据wlid、shelfname、valueslect确定唯一库存,并查出剩余库存量
    Integer selectCount(Long wlId, String shelfName, String valueSelect);
    //解锁库存
    void unlock(List<OutbillTaskEntity> list);
    //处理上架oenstock的消息，验证是不是要取消库存增加
    void downStock(OnestockupTaskEntity onestockupTaskEntity);
}

