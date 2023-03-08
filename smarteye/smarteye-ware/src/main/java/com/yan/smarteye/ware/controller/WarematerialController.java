package com.yan.smarteye.ware.controller;

import java.util.Arrays;
import java.util.Map;

import com.yan.common.to.WareMaterialTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.smarteye.ware.entity.WarematerialEntity;
import com.yan.smarteye.ware.service.WarematerialService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 库存品

 */
@RestController
@RequestMapping("ware/warematerial")
public class WarematerialController {
    @Autowired
    private WarematerialService warematerialService;

    /**
     * 向wms中添加warematerial库存(分库库存)
     */
    @RequestMapping("/add")
    public R add(@RequestBody WareMaterialTo wareMaterialTo){
        warematerialService.addMaterial(wareMaterialTo);
        return R.ok();
    }

    /**
     * 列表 条件查询
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = warematerialService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{warematerialId}")
    public R info(@PathVariable("warematerialId") Long warematerialId){
		WarematerialEntity warematerial = warematerialService.getById(warematerialId);

        return R.ok().put("data", warematerial);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WarematerialEntity warematerial){
		warematerialService.save(warematerial);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WarematerialEntity warematerial){
		warematerialService.updateById(warematerial);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] warematerialIds){
		warematerialService.removeByIds(Arrays.asList(warematerialIds));

        return R.ok();
    }

}
