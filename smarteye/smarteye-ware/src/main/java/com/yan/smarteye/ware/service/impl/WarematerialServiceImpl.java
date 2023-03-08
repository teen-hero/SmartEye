package com.yan.smarteye.ware.service.impl;

import com.yan.common.to.WareMaterialTo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.ware.dao.WarematerialDao;
import com.yan.smarteye.ware.entity.WarematerialEntity;
import com.yan.smarteye.ware.service.WarematerialService;


@Service("warematerialService")
public class WarematerialServiceImpl extends ServiceImpl<WarematerialDao, WarematerialEntity> implements WarematerialService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WarematerialEntity> page = this.page(
                new Query<WarematerialEntity>().getPage(params),
                new QueryWrapper<WarematerialEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 向wms中添加warematerial库存(分库库存)
     */
    @Override
    public void addMaterial(WareMaterialTo wareMaterialTo) {
        //1.先检查有没有该warematerial，有则更新数量，无则添加warematerial记录
        //根据wlid、shelfName和valueselect匹配唯一warematerial
        Long wlId = wareMaterialTo.getWlId();
        String valueSelect = wareMaterialTo.getValueSelect();
        String shelfName = wareMaterialTo.getShelfName();
        WarematerialEntity warematerialEntity = this.baseMapper.selectOne(new QueryWrapper<WarematerialEntity>().eq("wl_id", wlId).eq("value_select", valueSelect).eq("shelf_Name",shelfName));
        if (warematerialEntity!=null){ //有则更新数量
            warematerialEntity.setRealCount(warematerialEntity.getRealCount()+wareMaterialTo.getOnematerialCount());
            this.updateById(warematerialEntity);
        }else {   //无则添加warematerial记录
            WarematerialEntity entity = new WarematerialEntity();
            BeanUtils.copyProperties(wareMaterialTo,entity);
            entity.setRealCount(wareMaterialTo.getOnematerialCount());
            //默认预警库存、权限等级都为0
            entity.setLevelNumb(0);
            entity.setWarnCount(0);
            this.save(entity);
        }
    }

    //条件查询
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<WarematerialEntity> queryWrapper = new QueryWrapper<>();
        //拼接条件查询
        String shelfName = (String) params.get("shelfName");
        if (shelfName!=null && !shelfName.equals("")){
            queryWrapper.eq("shelf_name",shelfName);
        }
        String wlId = (String)params.get("wlppId");
        if (wlId!=null && !wlId.equals("")){
            Long id = new Long(wlId);
            queryWrapper.eq("wlpp_id",id);
        }

        //构造分页
        IPage<WarematerialEntity> page = this.page(
                new Query<WarematerialEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

}