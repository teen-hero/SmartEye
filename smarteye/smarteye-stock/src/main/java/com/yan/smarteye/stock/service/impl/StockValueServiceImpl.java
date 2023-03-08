package com.yan.smarteye.stock.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.stock.dao.StockValueDao;
import com.yan.smarteye.stock.entity.StockValueEntity;
import com.yan.smarteye.stock.service.StockValueService;


@Service("stockValueService")
public class StockValueServiceImpl extends ServiceImpl<StockValueDao, StockValueEntity> implements StockValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StockValueEntity> page = this.page(
                new Query<StockValueEntity>().getPage(params),
                new QueryWrapper<StockValueEntity>()
        );

        return new PageUtils(page);
    }

}