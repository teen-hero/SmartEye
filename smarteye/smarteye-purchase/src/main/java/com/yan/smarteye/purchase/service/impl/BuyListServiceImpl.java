package com.yan.smarteye.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yan.common.constant.PurchaseConstant;
import com.yan.smarteye.purchase.dao.BuyDemandDao;
import com.yan.smarteye.purchase.entity.BuyDemandEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.purchase.dao.BuyListDao;
import com.yan.smarteye.purchase.entity.BuyListEntity;
import com.yan.smarteye.purchase.service.BuyListService;
import org.springframework.transaction.annotation.Transactional;


@Service("buyListService")
public class BuyListServiceImpl extends ServiceImpl<BuyListDao, BuyListEntity> implements BuyListService {
    @Autowired
    BuyDemandDao buyDemandDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BuyListEntity> page = this.page(
                new Query<BuyListEntity>().getPage(params),
                new QueryWrapper<BuyListEntity>()
        );

        return new PageUtils(page);
    }

    //查询未领取（未分配）的采购单
    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        QueryWrapper<BuyListEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", PurchaseConstant.BuyListStatusEnum.CREATED.getCode());

        IPage<BuyListEntity> page = this.page(
                new Query<BuyListEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    //领取采购单status:2 或 分配采购单status:1  成功返回0，失败返回1
    @Transactional
    @Override
    public int doList(BuyListEntity buyListEntity) {
        //只有新建状态才可以领取或者分配
        BuyListEntity entity = baseMapper.selectById(buyListEntity.getBuyListId());
        if (entity.getStatus()!=PurchaseConstant.BuyListStatusEnum.CREATED.getCode()){
            return 1;
        }
        //1.领取 或 分配 采购单（他们的status不同）
        this.baseMapper.updateById(buyListEntity);
        //2.修改该采购单下的所有采购需求的状态为正在采购
        UpdateWrapper<BuyDemandEntity> updateWrapper = new UpdateWrapper<BuyDemandEntity>().eq("buy_list_id", buyListEntity.getBuyListId());
        BuyDemandEntity buyDemandEntity = new BuyDemandEntity();
        buyDemandEntity.setStatus(PurchaseConstant.BuyDemandStatusEnum.BUYING.getCode());
        buyDemandDao.update(buyDemandEntity,updateWrapper);
        return 0;
    }

}