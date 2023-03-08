package com.yan.smarteye.material.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yan.smarteye.material.entity.MaterialTypeShelfRelationEntity;
import com.yan.smarteye.material.service.MaterialTypeShelfRelationService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 库存种类&货架关联
 *
 */
@RestController
@RequestMapping("material/materialtypeshelfrelation")
public class MaterialTypeShelfRelationController {
    @Autowired
    private MaterialTypeShelfRelationService materialTypeShelfRelationService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = materialTypeShelfRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MaterialTypeShelfRelationEntity materialTypeShelfRelation = materialTypeShelfRelationService.getById(id);

        return R.ok().put("materialTypeShelfRelation", materialTypeShelfRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MaterialTypeShelfRelationEntity materialTypeShelfRelation){
		materialTypeShelfRelationService.save(materialTypeShelfRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MaterialTypeShelfRelationEntity materialTypeShelfRelation){
		materialTypeShelfRelationService.updateById(materialTypeShelfRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		materialTypeShelfRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
