package com.yan.smarteye.material.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.material.dao.AskmbillDao;
import com.yan.smarteye.material.entity.AskmbillEntity;
import com.yan.smarteye.material.service.AskmbillService;


@Service("askmbillService")
public class AskmbillServiceImpl extends ServiceImpl<AskmbillDao, AskmbillEntity> implements AskmbillService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AskmbillEntity> page = this.page(
                new Query<AskmbillEntity>().getPage(params),
                new QueryWrapper<AskmbillEntity>()
        );

        return new PageUtils(page);
    }

}