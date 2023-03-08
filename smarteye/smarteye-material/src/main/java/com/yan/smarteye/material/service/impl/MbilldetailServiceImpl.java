package com.yan.smarteye.material.service.impl;

import com.yan.common.constant.MaterialConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.material.dao.MbilldetailDao;
import com.yan.smarteye.material.entity.MbilldetailEntity;
import com.yan.smarteye.material.service.MbilldetailService;


@Service("mbilldetailService")
public class MbilldetailServiceImpl extends ServiceImpl<MbilldetailDao, MbilldetailEntity> implements MbilldetailService {

    @Autowired
    MbilldetailService mbilldetailService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MbilldetailEntity> page = this.page(
                new Query<MbilldetailEntity>().getPage(params),
                new QueryWrapper<MbilldetailEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查询mbillid下的所有申请详情
     */
    @Override
    public List<MbilldetailEntity> getByMbillId(Long mbillId) {
        QueryWrapper<MbilldetailEntity> queryWrapper = new QueryWrapper<MbilldetailEntity>().eq("mbill_id", mbillId);
        List<MbilldetailEntity> mbilldetailEntities = this.baseMapper.selectList(queryWrapper);
        return mbilldetailEntities;
    }

    /**
     * 物料申请确认送达
     *
     */
    @Override
    public void received(Long mbilldetailId) {
        //先判断是否是申请状态，只有申请状态的才可以改为送达状态
        QueryWrapper<MbilldetailEntity> queryWrapper = new QueryWrapper<MbilldetailEntity>().eq("mbilldetail_id", mbilldetailId);
        MbilldetailEntity entity = mbilldetailService.getOne(queryWrapper);
        if(entity.getStatus()== MaterialConstant.MbilldetailEunm.APPLYING.getCode()){
            entity.setStatus(MaterialConstant.MbilldetailEunm.RECEIVED.getCode());
        }
        this.baseMapper.updateById(entity);
    }

}