package com.yan.smarteye.material.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.smarteye.material.entity.AskmbillEntity;
import com.yan.smarteye.material.service.AskmbillService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 物料申请单详情

 */
@RestController
@RequestMapping("material/askmbill")
public class AskmbillController {
    @Autowired
    private AskmbillService askmbillService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = askmbillService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{askmbillId}")
    public R info(@PathVariable("askmbillId") Long askmbillId){
		AskmbillEntity askmbill = askmbillService.getById(askmbillId);

        return R.ok().put("askmbill", askmbill);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AskmbillEntity askmbill){
		askmbillService.save(askmbill);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AskmbillEntity askmbill){
		askmbillService.updateById(askmbill);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] askmbillIds){
		askmbillService.removeByIds(Arrays.asList(askmbillIds));

        return R.ok();
    }

}
