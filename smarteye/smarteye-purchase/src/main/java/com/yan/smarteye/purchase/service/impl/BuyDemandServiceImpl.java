package com.yan.smarteye.purchase.service.impl;

import com.yan.common.constant.PurchaseConstant;
import com.yan.common.to.LowWarnCountTo;
import com.yan.common.utils.ChangeList;
import com.yan.common.utils.R;
import com.yan.smarteye.purchase.dao.BuyListDao;
import com.yan.smarteye.purchase.entity.BuyListEntity;
import com.yan.smarteye.purchase.feign.WareStockFeign;
import com.yan.smarteye.purchase.vo.MergeVo;
import com.yan.smarteye.purchase.vo.WarnEntityInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.purchase.dao.BuyDemandDao;
import com.yan.smarteye.purchase.entity.BuyDemandEntity;
import com.yan.smarteye.purchase.service.BuyDemandService;
import org.springframework.transaction.annotation.Transactional;


@Service("buyDemandService")
public class BuyDemandServiceImpl extends ServiceImpl<BuyDemandDao, BuyDemandEntity> implements BuyDemandService {

    @Autowired
    BuyListDao buyListDao;
    @Autowired
    WareStockFeign wareStockFeign;
    @Autowired
    BuyDemandDao buyDemandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BuyDemandEntity> page = this.page(
                new Query<BuyDemandEntity>().getPage(params),
                new QueryWrapper<BuyDemandEntity>()
        );

        return new PageUtils(page);
    }

    //合并采购需求（就是添加到采购单即添加采购需求表中的采购单id）
    @Transactional
    @Override
    public List<Long> mergePurchase(MergeVo mergeVo) {
        Long buyListId = mergeVo.getBuyListId();
        // 如果采购id为null 说明没选采购单
        if (buyListId == null){
            //新建采购单
            BuyListEntity buyListEntity = new BuyListEntity();
            buyListEntity.setStatus(PurchaseConstant.BuyListStatusEnum.CREATED.getCode());
            //设置采购单默认名
            buyListEntity.setBuyListName("默认采购单"+new Date());
            buyListDao.insert(buyListEntity);
            buyListId = buyListEntity.getBuyListId();
        }
        //合并采购需求（就是添加到采购单即添加采购需求表中的采购单id）
        List<Long> items = mergeVo.getItems();

        Long finalBuyListId = buyListId;
        List<BuyDemandEntity> buyDemandEntities = baseMapper.selectBatchIds(items);
        List<Long> unMergeIds = new ArrayList<>();
        for (BuyDemandEntity buyDemandEntity: buyDemandEntities){  //只有新建状态可以合并采购需求
            if (buyDemandEntity.getStatus()==PurchaseConstant.BuyDemandStatusEnum.CREATED.getCode()){
                buyDemandEntity.setBuyListId(finalBuyListId);  //设置采购单id
                buyDemandEntity.setStatus(PurchaseConstant.BuyDemandStatusEnum.ASSIGNED.getCode()); //修改状态
            }else {
                //未合并的返回前端提醒
                unMergeIds.add(buyDemandEntity.getBuyDemandId());
            }
        }
        this.updateBatchById(buyDemandEntities);
        return unMergeIds;
    }

    //自动检测预警库存，生成采购需求
    @Override
    public List<WarnEntityInfoVo> autoRemand() {
        //1.远程调用，返回低于预警库存的货物
        R res = wareStockFeign.warnCount();
        //赋值并保存
       // List<LowWarnCountTo> tos = (List<LowWarnCountTo>)(res.get("data"));  该方式无法强转成功
        List<LowWarnCountTo> tos= ChangeList.changeList(res.get("data"), LowWarnCountTo.class);

       //使用迭代器遍历
        Iterator<LowWarnCountTo> iterator = tos.iterator();
        List<BuyDemandEntity> list = new ArrayList<>();
        List<WarnEntityInfoVo> vos = new ArrayList<>();
        while (iterator.hasNext()){
            LowWarnCountTo to = iterator.next();
            //如果该货物存在（新建、采购中、已分配）状态的采购需求，则不必重复自动创建该货物的采购需求
            QueryWrapper<BuyDemandEntity> queryWrapper = new QueryWrapper<BuyDemandEntity>()
                    .eq("warestock_id", to.getWarestockId())
                    .in("status",PurchaseConstant.BuyDemandStatusEnum.ASSIGNED.getCode(),PurchaseConstant.BuyDemandStatusEnum.CREATED.getCode(),PurchaseConstant.BuyDemandStatusEnum.BUYING.getCode());
            List<BuyDemandEntity> buyDemandEntities = buyDemandDao.selectList(queryWrapper);
            if(buyDemandEntities==null || buyDemandEntities.size()==0){ //如果该货物存在（新建、采购中、已分配）状态的采购需求，则不必重复自动创建该货物的采购需求
                BuyDemandEntity buyDemandEntity = new BuyDemandEntity();
                BeanUtils.copyProperties(to,buyDemandEntity);
                //自动生成的采购需求，来源设置为1
                buyDemandEntity.setOrigin(1);
                //设置状态为新建
                buyDemandEntity.setStatus(PurchaseConstant.BuyDemandStatusEnum.CREATED.getCode());
                list.add(buyDemandEntity);
                //此外，将信息封装到vo返回，以作他用
                WarnEntityInfoVo vo = new WarnEntityInfoVo();
                BeanUtils.copyProperties(to,vo);
                vos.add(vo);
            }
        }
        //保存/生成采购需求
        this.saveBatch(list);
        //将信息封装到vo返回，以作他用
        return vos;
    }

}