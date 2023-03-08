package com.yan.smarteye.material.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.smarteye.material.entity.MaterialImagesEntity;
import com.yan.smarteye.material.service.MaterialImagesService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 库存货物的图片集

 */
@RestController
@RequestMapping("material/materialimages")
public class MaterialImagesController {
    @Autowired
    private MaterialImagesService materialImagesService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = materialImagesService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MaterialImagesEntity materialImages = materialImagesService.getById(id);

        return R.ok().put("materialImages", materialImages);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MaterialImagesEntity materialImages){
		materialImagesService.save(materialImages);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MaterialImagesEntity materialImages){
		materialImagesService.updateById(materialImages);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		materialImagesService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
