package com.yan.smarteye.purchase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.purchase.entity.BuyDemandEntity;
import com.yan.smarteye.purchase.vo.MergeVo;
import com.yan.smarteye.purchase.vo.WarnEntityInfoVo;

import java.util.List;
import java.util.Map;

/**
 * 采购需求

 */
public interface BuyDemandService extends IService<BuyDemandEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //合并采购需求（就是添加到采购单即添加采购需求表中的采购单id）
    List<Long> mergePurchase(MergeVo mergeVo);
    //自动检测预警库存，生成采购需求
    List<WarnEntityInfoVo> autoRemand();
}

