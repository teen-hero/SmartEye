package com.yan.smarteye.material.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.material.dao.MaterialImagesDao;
import com.yan.smarteye.material.entity.MaterialImagesEntity;
import com.yan.smarteye.material.service.MaterialImagesService;


@Service("materialImagesService")
public class MaterialImagesServiceImpl extends ServiceImpl<MaterialImagesDao, MaterialImagesEntity> implements MaterialImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MaterialImagesEntity> page = this.page(
                new Query<MaterialImagesEntity>().getPage(params),
                new QueryWrapper<MaterialImagesEntity>()
        );

        return new PageUtils(page);
    }

}