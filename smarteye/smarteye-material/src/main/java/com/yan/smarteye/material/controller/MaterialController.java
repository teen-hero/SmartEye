package com.yan.smarteye.material.controller;

import java.util.Arrays;
import java.util.Map;

import com.yan.smarteye.material.vo.saveVo.MaterialVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.smarteye.material.entity.MaterialEntity;
import com.yan.smarteye.material.service.MaterialService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 待上架或已上架的一批货物

 */
@RestController
@RequestMapping("material/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    /**
     * 保存material及onematerial,入库操作
     */
    @RequestMapping("/save")
    public R save(@RequestBody MaterialVo materialVo){
        materialService.saveInfo(materialVo);

        return R.ok();
    }

    /**
     * 列表,检索查询
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = materialService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 上架material
     */
    @RequestMapping("/{materialId}/up")
    public R up(@PathVariable("materialId") Long materialId){
        //返回0代表onematerial没全部上架material上架失败,1代表material上架成功
        int upRes = materialService.up(materialId);

        return R.ok().put("data", upRes);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{materialId}")
    public R info(@PathVariable("materialId") Long materialId){
		MaterialEntity material = materialService.getById(materialId);

        return R.ok().put("material", material);
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MaterialEntity material){
		materialService.updateById(material);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] materialIds){
		materialService.removeByIds(Arrays.asList(materialIds));

        return R.ok();
    }

}
