package com.yan.smarteye.stock.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.yan.smarteye.stock.entity.OutbillEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.smarteye.stock.entity.OutbilldetailEntity;
import com.yan.smarteye.stock.service.OutbilldetailService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 出库单详情
 */
@RestController
@RequestMapping("stock/outbilldetail")
public class OutbilldetailController {
    @Autowired
    private OutbilldetailService outbilldetailService;




    /**
     * 查询outbillid下的所有出库详情
     */
    @RequestMapping("{outbillId}/info")
    public R millinfo(@PathVariable("outbillId") Long outbillId){
        List<OutbilldetailEntity> res = outbilldetailService.getByOutbillId(outbillId);

        return R.ok().put("data", res);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outbilldetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{outbilldetailId}")
    public R info(@PathVariable("outbilldetailId") Long outbilldetailId){
		OutbilldetailEntity outbilldetail = outbilldetailService.getById(outbilldetailId);

        return R.ok().put("outbilldetail", outbilldetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OutbilldetailEntity outbilldetail){
		outbilldetailService.save(outbilldetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OutbilldetailEntity outbilldetail){
		outbilldetailService.updateById(outbilldetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] outbilldetailIds){
		outbilldetailService.removeByIds(Arrays.asList(outbilldetailIds));

        return R.ok();
    }

}
