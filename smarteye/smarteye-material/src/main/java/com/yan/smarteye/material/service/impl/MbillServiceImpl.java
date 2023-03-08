package com.yan.smarteye.material.service.impl;

import com.yan.common.constant.MaterialConstant;
import com.yan.smarteye.material.dao.MbilldetailDao;
import com.yan.smarteye.material.entity.MbilldetailEntity;
import com.yan.smarteye.material.service.WareLocationService;
import com.yan.smarteye.material.vo.MouthMbillbingtuVo;
import com.yan.smarteye.material.vo.MouthMbillRespVo;
import com.yan.smarteye.material.vo.savembillvo.MbillVo;
import com.yan.smarteye.material.vo.savembillvo.MbilldetialVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.material.dao.MbillDao;
import com.yan.smarteye.material.entity.MbillEntity;
import com.yan.smarteye.material.service.MbillService;
import org.springframework.transaction.annotation.Transactional;


@Service("mbillService")
public class MbillServiceImpl extends ServiceImpl<MbillDao, MbillEntity> implements MbillService {

    @Autowired
    WareLocationService wareLocationService;
    @Autowired
    MbilldetailDao mbilldetailDao;
    @Autowired
    MbillDao mbillDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MbillEntity> page = this.page(
                new Query<MbillEntity>().getPage(params),
                new QueryWrapper<MbillEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存申请表、申请表详情
     */
    @Transactional
    @Override
    public void saveInfo(MbillVo mbillVo) {
        //1、保存物料申请单
        MbillEntity mbillEntity = new MbillEntity();
        BeanUtils.copyProperties(mbillVo,mbillEntity);
        //状态设置为0 TODO 应该先做一个常量，代表0的含义，如StockConstant类
        mbillEntity.setStatus(0);
        //时间设置通过mp在实体类标注时间字段的自动填充实现
        this.save(mbillEntity);

        //2.保存物料申请表详情
        for (MbilldetialVo vo : mbillVo.getMbilldetialVos()){
            MbilldetailEntity mbilldetailEntity = new MbilldetailEntity();
            BeanUtils.copyProperties(vo,mbilldetailEntity);

            //设置对应的mbill的Id
            mbilldetailEntity.setMbillId(mbillEntity.getMbillId());
            //设置wlId、wlpId、wlppId
            mbilldetailEntity.setWlId(mbillEntity.getWlId());
            Long[] catelogPath = wareLocationService.findCatelogPath(mbillEntity.getWlId());
            mbilldetailEntity.setWlpId(catelogPath[1]);
            mbilldetailEntity.setWlppId(catelogPath[0]);
            //状态默认设置为0
            mbilldetailEntity.setStatus(0);
            //时间设置通过mp在实体类标注时间字段的自动填充实现
            mbilldetailDao.insert(mbilldetailEntity);
        }


    }

    /**
     * 撤回申请
     */
    @Transactional
    @Override
    public int revoke(Long mbillId) {
        //1.修改申请单状态
        MbillEntity mbillEntity = this.getById(mbillId);
        Integer status = mbillEntity.getStatus();
        if (status!=MaterialConstant.MbillEunm.APPLYING.getCode()){  //不是申请中的单子无法撤回
            return 0;
        }
        mbillEntity.setStatus(MaterialConstant.MbillEunm.REVOKE.getCode());
        this.updateById(mbillEntity);
        //2.修改申请单对应的申请单详情状态
        //TODO 这里应该单独写sql的，set .. where ...，，偷懒用mp实现了
        QueryWrapper<MbilldetailEntity> queryWrapper = new QueryWrapper<MbilldetailEntity>().eq("mbill_id", mbillId);
        List<MbilldetailEntity> mbilldetailEntities = mbilldetailDao.selectList(queryWrapper);
        for (MbilldetailEntity entity : mbilldetailEntities){
            entity.setStatus(MaterialConstant.MbilldetailEunm.REVOKE.getCode());
            mbilldetailDao.updateById(entity);
        }
        return 1;
    }

    /**
     * 审批物料申请,status 1为处理中，3为拒绝申请
     */
    @Override
    public int handle(Long mbillId,int status) {
        //1.修改申请单状态
        MbillEntity mbillEntity = this.getById(mbillId);
        Integer mbillEntityStatus = mbillEntity.getStatus();
        if (mbillEntityStatus!=MaterialConstant.MbillEunm.APPLYING.getCode()) {  //不是申请中的单子无法进行审批操作
            return 0;
        }
        if (status == 1){
            mbillEntity.setStatus(MaterialConstant.MbillEunm.PROCESSING.getCode());
        }
        if (status == 3){
            mbillEntity.setStatus(MaterialConstant.MbillEunm.REFUSE.getCode());
        }
        this.updateById(mbillEntity);
        //2.修改申请单对应的申请单详情状态
        //TODO 这里应该单独写sql的，set .. where ...，，偷懒用mp实现了
        QueryWrapper<MbilldetailEntity> queryWrapper = new QueryWrapper<MbilldetailEntity>().eq("mbill_id", mbillId);
        List<MbilldetailEntity> mbilldetailEntities = mbilldetailDao.selectList(queryWrapper);
        for (MbilldetailEntity entity : mbilldetailEntities){
            if (status == 3){
                entity.setStatus(MaterialConstant.MbilldetailEunm.REFUSE.getCode());
            }
            mbilldetailDao.updateById(entity);
        }
        return 1;
    }

    /**
     * 查询今天新增物料申请单数量
     */
    @Override
    public int queryTodayMbill() {
        int res = mbillDao.queryTodayMbill();
        return res;
    }

    /**
     * 待处理的物料申请单数量
     */
    @Override
    public int queryTodoMbill() {
        int res = mbillDao.queryTodoMbill();
        return res;
    }

    /**
     * 查询近一个月物料申请单的数量趋势，以天为分组
     */
    @Override
    public List<MouthMbillRespVo> queryMouthMbill() {
        List<MouthMbillRespVo> res =mbillDao.queryMouthMbill();
        return res;
    }

    //查询近一个月物料申请单来自的分库的分布（饼图）
    @Override
    public List<MouthMbillbingtuVo> queryMouthMbillbingtu() {
        List<MouthMbillbingtuVo> res = mbillDao.queryMouthMbillbingtu();
        return res;
    }

}