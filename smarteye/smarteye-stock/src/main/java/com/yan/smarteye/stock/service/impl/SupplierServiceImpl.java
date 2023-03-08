package com.yan.smarteye.stock.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.stock.dao.SupplierDao;
import com.yan.smarteye.stock.entity.SupplierEntity;
import com.yan.smarteye.stock.service.SupplierService;


@Service("supplierService")
public class SupplierServiceImpl extends ServiceImpl<SupplierDao, SupplierEntity> implements SupplierService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SupplierEntity> page = this.page(
                new Query<SupplierEntity>().getPage(params),
                new QueryWrapper<SupplierEntity>()
        );

        return new PageUtils(page);
    }

}