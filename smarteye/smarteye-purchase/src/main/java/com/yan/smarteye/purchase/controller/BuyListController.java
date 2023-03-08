package com.yan.smarteye.purchase.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.smarteye.purchase.entity.BuyListEntity;
import com.yan.smarteye.purchase.service.BuyListService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 采购单
 */
@RestController
@RequestMapping("purchase/buylist")
public class BuyListController {
    @Autowired
    private BuyListService buyListService;

    //领取采购单status:2 或 分配采购单status:1  成功返回0，失败返回1
    @RequestMapping("/dolist")
    public R dolist(@RequestBody BuyListEntity buyListEntity){
        int res = buyListService.doList(buyListEntity);
        return R.ok().put("data",res);
    }


    //查询未领取（未分配）的采购单
    @RequestMapping("/unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = buyListService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = buyListService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{buyListId}")
    public R info(@PathVariable("buyListId") Long buyListId){
		BuyListEntity buyList = buyListService.getById(buyListId);

        return R.ok().put("data", buyList);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody BuyListEntity buyList){
		buyListService.save(buyList);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BuyListEntity buyList){
		buyListService.updateById(buyList);

        return R.ok();
    }

    /**
     * 删除  TODO 采购单的删除与采购需求要级联修改
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] buyListIds){
		buyListService.removeByIds(Arrays.asList(buyListIds));

        return R.ok();
    }

}
