package com.yan.smarteye.stock.service.impl;

import com.yan.smarteye.stock.dao.ShelfDao;
import com.yan.smarteye.stock.vo.ShelfRelationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.stock.dao.StockTypeShelfRelationDao;
import com.yan.smarteye.stock.entity.StockTypeShelfRelationEntity;
import com.yan.smarteye.stock.service.StockTypeShelfRelationService;


@Service("stockTypeShelfRelationService")
public class StockTypeShelfRelationServiceImpl extends ServiceImpl<StockTypeShelfRelationDao, StockTypeShelfRelationEntity> implements StockTypeShelfRelationService {

    @Autowired
    ShelfDao shelfDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StockTypeShelfRelationEntity> page = this.page(
                new Query<StockTypeShelfRelationEntity>().getPage(params),
                new QueryWrapper<StockTypeShelfRelationEntity>()
        );

        return new PageUtils(page);
    }

    //添加库存种类与货架关联关系
    @Override
    public void saveBatch(List<ShelfRelationVo> vos) {
        //接受到vos后把其转为AttrAttrgroupRelationEntity
        //再调用批量保存方法进行保存
        List<StockTypeShelfRelationEntity> collect = vos.stream().map(item -> {
            StockTypeShelfRelationEntity relationEntity = new StockTypeShelfRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }


}