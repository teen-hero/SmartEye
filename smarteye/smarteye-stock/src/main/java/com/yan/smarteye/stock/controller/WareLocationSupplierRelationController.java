package com.yan.smarteye.stock.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yan.smarteye.stock.entity.SupplierEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yan.smarteye.stock.entity.WareLocationSupplierRelationEntity;
import com.yan.smarteye.stock.service.WareLocationSupplierRelationService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 库存分区&供用商关联
 */
@RestController
@RequestMapping("stock/warelocationsupplierrelation")
public class WareLocationSupplierRelationController {
    @Autowired
    private WareLocationSupplierRelationService wareLocationSupplierRelationService;


    /**
     * 列表,获取当前品牌关联的所有分类列表
     */
    @RequestMapping("/warelocation/list")
    public R warelocationloglist(@RequestParam("supplierId")Long supplierId){
        List<WareLocationSupplierRelationEntity> data = wareLocationSupplierRelationService.list(
                new QueryWrapper<WareLocationSupplierRelationEntity>().eq("supplier_id",supplierId)
        );

        return R.ok().put("data", data);
    }

    /**
     * 保存,saveDetail
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareLocationSupplierRelationEntity wareLocationSupplierRelationEntity){
        wareLocationSupplierRelationService.saveDetail(wareLocationSupplierRelationEntity);

        return R.ok();
    }


    /**
     *  新增货物时，点击货物的分区，要获取与该分区关联的所有供用商
     */
    @GetMapping("/supplier/list")
    public R relationBrandsList(@RequestParam(value = "wlId",required = true)Long wlId){
        //据一个分类id查询和他有关的所有供用商
        List<SupplierEntity> entities = wareLocationSupplierRelationService.getSupplierByWlId(wlId);

        return R.ok().put("data",entities);

    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareLocationSupplierRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareLocationSupplierRelationEntity wareLocationSupplierRelation = wareLocationSupplierRelationService.getById(id);

        return R.ok().put("wareLocationSupplierRelation", wareLocationSupplierRelation);
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareLocationSupplierRelationEntity wareLocationSupplierRelation){
		wareLocationSupplierRelationService.updateById(wareLocationSupplierRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareLocationSupplierRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
