package com.yan.smarteye.material.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.material.entity.MaterialEntity;
import com.yan.smarteye.material.vo.saveVo.MaterialVo;

import java.util.Map;

/**
 * 待上架或已上架的一批货物

 */
public interface MaterialService extends IService<MaterialEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //保存material及onematerial,入库操作
    void saveInfo(MaterialVo materialVo);
    //检索查询
    PageUtils queryPageByCondition(Map<String, Object> params);
    //上架material ,返回0代表onematerial没全部上架material上架失败,1代表material上架成功
    int up(Long materialId);
}

