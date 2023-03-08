package com.yan.smarteye.ware.service.impl;


import com.alibaba.fastjson.TypeReference;
import com.yan.common.constant.RabbitMQConstant;
import com.yan.common.to.LowWarnCountTo;
import com.yan.common.to.WareStockRespTo;
import com.yan.common.to.WareStockTo;
import com.yan.common.utils.*;
import com.yan.smarteye.ware.dao.OutbillTaskDao;
import com.yan.smarteye.ware.entity.OnestockupTaskEntity;
import com.yan.smarteye.ware.entity.OutbillTaskEntity;
import com.yan.smarteye.ware.exception.NoStockException;
import com.yan.smarteye.ware.exception.NullWareStock;
import com.yan.smarteye.ware.feign.StockFeign;
import com.yan.smarteye.ware.service.OnestockupTaskService;
import com.yan.smarteye.ware.service.OutbillTaskService;
import com.yan.smarteye.ware.vo.CatalogueVo;
import com.yan.smarteye.ware.vo.OutbilldetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yan.smarteye.ware.dao.WarestockDao;
import com.yan.smarteye.ware.entity.WarestockEntity;
import com.yan.smarteye.ware.service.WarestockService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("warestockService")
public class WarestockServiceImpl extends ServiceImpl<WarestockDao, WarestockEntity> implements WarestockService {

    @Autowired
    StockFeign wareLocationFeign;
    @Autowired
    WarestockDao warestockDao;
    @Autowired
    OutbillTaskDao outbillTaskDao;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    StockFeign stockFeign;
    @Autowired
    OnestockupTaskService onestockupTaskService;
    @Autowired
    OutbillTaskService outbillTaskService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WarestockEntity> page = this.page(
                new Query<WarestockEntity>().getPage(params),
                new QueryWrapper<WarestockEntity>()
        );

        return new PageUtils(page);
    }

    //向wms中添加warestock库存
    @Override
    public void add(WareStockTo wareStockTo) {
        //1.先检查有没有该warestock，有则更新数量，无则添加warestock记录
        //根据wlid、shelfName和valueselect匹配唯一warestock
        Long wlId = wareStockTo.getWlId();
        String valueSelect = wareStockTo.getValueSelect();
        String shelfName = wareStockTo.getShelfName();
        WarestockEntity warestockEntity = this.baseMapper.selectOne(new QueryWrapper<WarestockEntity>().eq("wl_id", wlId).eq("value_select", valueSelect).eq("shelf_Name",shelfName));
        if (warestockEntity!=null){ //有则更新数量
            warestockEntity.setRealCount(warestockEntity.getRealCount()+wareStockTo.getOnestockCount());
            this.updateById(warestockEntity);
        }else {   //无则添加warestock记录
            WarestockEntity entity = new WarestockEntity();
            BeanUtils.copyProperties(wareStockTo,entity);
            entity.setRealCount(wareStockTo.getOnestockCount());
            //默认预警库存、权限等级都为0
            entity.setLevelNumb(0);
            entity.setWarnCount(0);
            this.save(entity);
        }
        //保存任务单信息（任务单有个状态属性，也起到防止消息重复消费的作用）
        //向消息队列中发消息，告知库存添加成功，延时队列实现分布式事务最终一致性
        OnestockupTaskEntity onestockupTaskEntity = new OnestockupTaskEntity();
        onestockupTaskEntity.setOnestockId(wareStockTo.getOnestockId());
        onestockupTaskEntity.setWarestockId(warestockEntity.getWarestockId());
        onestockupTaskEntity.setUpNum(wareStockTo.getOnestockCount());
        onestockupTaskEntity.setStatus(0);
        onestockupTaskService.save(onestockupTaskEntity);
        rabbitTemplate.convertAndSend(RabbitMQConstant.WARE_EXCHANGE,RabbitMQConstant.WARE_ONESTOCKUPROUTINGKEY,onestockupTaskEntity);
    }

    //处理上架oenstock的消息，验证是不是要取消库存增加
    @Override
    public void downStock(OnestockupTaskEntity onestockupTaskEntity) {
        Long onestockId = onestockupTaskEntity.getOnestockId();
        R r = stockFeign.ontstockInfo(onestockId);
        OnestockupTaskEntity entity = r.getData("data", new TypeReference<OnestockupTaskEntity>() {
        });
        //状态0说明onestock没上架成功，要回滚增加的库存
        if(entity!=null && entity.getStatus()==0){
            OnestockupTaskEntity taskEntity = onestockupTaskService.getById(onestockupTaskEntity);
            if(taskEntity.getStatus()==0){
                //取消库存
                Long warestockId = taskEntity.getWarestockId();
                WarestockEntity warestockEntity = warestockDao.selectById(warestockId);
                warestockEntity.setRealCount(warestockEntity.getRealCount()-taskEntity.getUpNum());
                warestockDao.updateById(warestockEntity);
            }
        }
    }


    //条件查询
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<WarestockEntity> queryWrapper = new QueryWrapper<>();
        //拼接条件查询
        String shelfName = (String) params.get("shelfName");
        if (shelfName!=null && !shelfName.equals("")){
            queryWrapper.eq("shelf_name",shelfName);
        }
        String wlId = (String)params.get("wlId");
        if (wlId!=null && !wlId.equals("")){
            Long id = new Long(wlId);
            queryWrapper.eq("wl_id",id);
        }
        Long wlppId = (Long)params.get("wlppId");
        if (wlppId!=null){
            queryWrapper.eq("wlpp_id",wlppId);
        }
        Long wlpId = (Long)params.get("wlpId");
        if (wlpId!=null){
            queryWrapper.eq("wlp_id",wlpId);
        }
        //构造分页
        IPage<WarestockEntity> page = this.page(
                new Query<WarestockEntity>().getPage(params),
                queryWrapper
        );
        //将Warestock中的wlid、wlpid、wlppid查询转为对应名称
        //TODO 思考：远程调用，发一个大的json 和 只发部分有用json，返回后再遍历拼装哪个更优
        List<WarestockEntity> entities = page.getRecords();
        List<WareStockRespTo> tos = new ArrayList<>();
        for (WarestockEntity entity:entities){
            WareStockRespTo to = new WareStockRespTo();
            BeanUtils.copyProperties(entity,to);
            tos.add(to);
        }

        //远程调用，设置好对应名字并返回
        R res = wareLocationFeign.transform(tos);
        List<WareStockRespTo> data = (List<WareStockRespTo>) res.get("data");
        //其实没必要再封装，直接返回lsit的data就行（就是分页查询的数据），只不过为了符合该方法返回值为PageUtils
        PageUtils pageUtils = new PageUtils();
        pageUtils.setList(data);
        pageUtils.setTotalCount((int)page.getTotal());
        pageUtils.setCurrPage((int)page.getCurrent());
        return pageUtils;
    }

    //返回低于预警库存的货物id、valueSelect(远程调用使用到)
    @Override
    public List<LowWarnCountTo> getLowWarnCount() {
        //查出低于预警库存的货物id、valueSelect
        List<WarestockEntity> list =  warestockDao.getLowWarnCount();
        //赋值给to
        List<LowWarnCountTo> tos = new ArrayList<>();
        for (WarestockEntity warestockEntity:list){
            LowWarnCountTo to = new LowWarnCountTo();
            BeanUtils.copyProperties(warestockEntity,to);
            tos.add(to);
        }
        return tos;
    }

    //返回低于预警库存的货物（分页）
    @Override
    public PageUtils getDangerCount(Map<String, Object> params) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(params.get(Constant.PAGE) != null){
            curPage = Long.parseLong((String)params.get(Constant.PAGE));
        }
        if(params.get(Constant.LIMIT) != null){
            limit = Long.parseLong((String)params.get(Constant.LIMIT));
        }

        //查出低于预警库存的货物（分页）
        List<WarestockEntity> list =  warestockDao.getDangerCount(curPage,limit);
        //查询低于预警库存的总数
        int size = warestockDao.getLowWarnCount().size();

        //将Warestock中的wlid、wlpid、wlppid查询转为对应名称
        //TODO 思考：远程调用，发一个大的json 和 只发部分有用json，返回后再遍历拼装哪个更优
        List<WareStockRespTo> tos = new ArrayList<>();
        for (WarestockEntity entity:list){
            WareStockRespTo to = new WareStockRespTo();
            BeanUtils.copyProperties(entity,to);
            tos.add(to);
        }

        //远程调用，设置好对应名字并返回
        R res = wareLocationFeign.transform(tos);
        List<WareStockRespTo> data = (List<WareStockRespTo>) res.get("data");
        PageUtils pageUtils = new PageUtils();
        pageUtils.setList(data);
        pageUtils.setTotalCount(size);
        pageUtils.setCurrPage((int)curPage);
        return pageUtils;
    }

    /**
     * 生成出库单后，立即锁定库存 （锁定库存）
     * 本地事务
     */
    @Transactional
    @Override
    public void billOut(List<OutbilldetailVo> vos) throws NoStockException,NullWareStock{
        QueryWrapper<WarestockEntity> queryWrapper;
        List<OutbillTaskEntity> list = new ArrayList<>();
        for (OutbilldetailVo vo : vos){
            //根据shelfName、valueSelect、wlId确定唯一warestock
            String shelfName = vo.getShelfName();
            String valueSelect = vo.getValueSelect();
            Long wlId = vo.getWlId();
            queryWrapper = new QueryWrapper<WarestockEntity>().eq("wl_id", wlId).eq("value_select", valueSelect).eq("shelf_Name", shelfName);
            WarestockEntity warestockEntity = this.baseMapper.selectOne(queryWrapper);

            if (warestockEntity==null){ //出库单中包含不存在的货物
                throw new NullWareStock();
            }

            if(warestockEntity.getRealCount() - warestockEntity.getLockCount() < vo.getOutbilldetailCount()){
                throw new NoStockException(); //库存不足
            }
            //锁定库存
            warestockEntity.setLockCount(warestockEntity.getLockCount() + vo.getOutbilldetailCount());
            this.updateById(warestockEntity);
            //保存锁库存工作单
            OutbillTaskEntity outbillTaskEntity = new OutbillTaskEntity();
            outbillTaskEntity.setOutbillId(vo.getOutbillId());
            outbillTaskEntity.setLockNum(vo.getOutbilldetailCount());
            outbillTaskEntity.setStatus(0);
            outbillTaskEntity.setShelfName(vo.getShelfName());
            outbillTaskEntity.setValueSelect(vo.getValueSelect());
            outbillTaskEntity.setWlId(vo.getWlId());
            outbillTaskDao.insert(outbillTaskEntity);
            list.add(outbillTaskEntity);
        }
        //锁定库存并保存库存锁定任务单后，向mq发消息，将该出库单对应的库存锁定信息发出去
        rabbitTemplate.convertAndSend(RabbitMQConstant.WARE_EXCHANGE,RabbitMQConstant.WARE_DELAYTOUTINGKEY,list);

    }

    //解锁库存
    //根据消息list中的出库单id，查找是不是真正生成了对应出库单而没回滚，若消息队列消息中的出库单id没有匹配的出库单，说明出库单回滚了
    @Transactional
    @Override
    public void unlock(List<OutbillTaskEntity> list) {
        //空消息无需解锁
        if(list==null || list.size()==0){
            return;
        }
        //从消息中获取id
        Long outbillId = list.get(0).getOutbillId();
        //远程查询是否存在此id的出库单
        R r = stockFeign.info(outbillId);
        if(r.getCode()==0){
            //订单不存在,解锁库存
            if(r.get("data") == null){
                for(OutbillTaskEntity outbillTaskEntity: list){
                    OutbillTaskEntity taskEntity = outbillTaskService.getById(outbillTaskEntity);
                    //解锁前先判断是否已经解锁了（因为用户创建完出库单可能立马点击撤回，撤回也会触发库存解锁，防止重复解锁）
                    if(taskEntity.getStatus()==1){
                        break; //因为该消息中所有的任务详情都是一个订单的，一个解锁说明都解锁过了，直接break即可
                    }
                    //解锁库存
                    String shelfName = outbillTaskEntity.getShelfName();
                    String valueSelect = outbillTaskEntity.getValueSelect();
                    Long wlId = outbillTaskEntity.getWlId();
                    WarestockEntity entity = warestockDao.selectOne(new QueryWrapper<WarestockEntity>().eq("shelf_name", shelfName)
                                                .eq("value_select", valueSelect).eq("wl_id", wlId));
                    entity.setLockCount(entity.getLockCount() - outbillTaskEntity.getLockNum());
                    warestockDao.updateById(entity);
                    //工作单状态改为1，起到防止重复消费消息的作用
                    taskEntity.setStatus(1);
                    outbillTaskService.updateById(taskEntity);
                }
            }
        }else{
            //远程调用异常，抛出异常让消息拒收，重新回归队列，等待重新释放
            throw new RuntimeException("解锁库存时的远程调用失败");
        }

    }




    /**
     * 出库可供选择目录
     */
    @Override
    public List<CatalogueVo> listWithGroup(int group) {
        //借助分组查询，查询库存中存在的目录
        //根据传入的group进行分组
        String column = "value_select";  //默认以value_select分组
        if (group==1){
            column = "wl_id";
        }
        if (group == 2){
            column = "wlp_id";
        }
        if (group == 3){
            column = "wlpp_id";
        }
        List<WarestockEntity> groups = this.baseMapper.selectList(new QueryWrapper<WarestockEntity>().groupBy(column));
        List<WareStockRespTo> tos= new ArrayList<>();
        for (WarestockEntity entity:groups){
            WareStockRespTo to = new WareStockRespTo();
            BeanUtils.copyProperties(entity,to);
            tos.add(to);
        }
        R res = wareLocationFeign.transform(tos);
        List<WareStockRespTo> groupsRes = ChangeList.changeList(res, WareStockRespTo.class);
        //翻译好之后,传入CatalogueVo返回
        List<CatalogueVo> catalogueVos = new ArrayList<>();
        for (WareStockRespTo to: groupsRes){
            CatalogueVo catalogueVo = new CatalogueVo();
            //不同分组情况分别设置
            if (group==1){
                catalogueVo.setName(to.getWlName());
                catalogueVo.setId(to.getWlId());
            }else if (group == 2){
                catalogueVo.setName(to.getWlpName());
                catalogueVo.setId(to.getWlpId());
            }else if (group == 3){
                catalogueVo.setName(to.getWlppName());
                catalogueVo.setId(to.getWlppId());
            }else{
                catalogueVo.setName(to.getValueSelect());
            }
            catalogueVos.add(catalogueVo);
        }
        return catalogueVos;
    }

    //根据wlid、shelfname、valueslect确定唯一库存,并查出剩余库存量
    @Override
    public Integer selectCount(Long wlId, String shelfName, String valueSelect) {
        QueryWrapper<WarestockEntity> queryWrapper = new QueryWrapper<WarestockEntity>().eq("wl_id", wlId).eq("shelf_name", shelfName).eq("value_select", valueSelect);
        WarestockEntity entity = this.baseMapper.selectOne(queryWrapper);
        if (entity==null){  //不存该库存返回-1
            return -1;
        }
        return entity.getRealCount();
    }



}