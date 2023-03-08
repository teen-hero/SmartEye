package com.yan.smarteye.stock.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.stock.dao.StockImagesDao;
import com.yan.smarteye.stock.entity.StockImagesEntity;
import com.yan.smarteye.stock.service.StockImagesService;


@Service("stockImagesService")
public class StockImagesServiceImpl extends ServiceImpl<StockImagesDao, StockImagesEntity> implements StockImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StockImagesEntity> page = this.page(
                new Query<StockImagesEntity>().getPage(params),
                new QueryWrapper<StockImagesEntity>()
        );

        return new PageUtils(page);
    }

}