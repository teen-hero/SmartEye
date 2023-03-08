package com.yan.smarteye.material.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.material.dao.MaterialValueDao;
import com.yan.smarteye.material.entity.MaterialValueEntity;
import com.yan.smarteye.material.service.MaterialValueService;


@Service("materialValueService")
public class MaterialValueServiceImpl extends ServiceImpl<MaterialValueDao, MaterialValueEntity> implements MaterialValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MaterialValueEntity> page = this.page(
                new Query<MaterialValueEntity>().getPage(params),
                new QueryWrapper<MaterialValueEntity>()
        );

        return new PageUtils(page);
    }

}