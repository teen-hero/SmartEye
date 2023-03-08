package com.yan.smarteye.stock.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.smarteye.stock.entity.StockValueEntity;
import com.yan.smarteye.stock.service.StockValueService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 货物的库存种类值
 */
@RestController
@RequestMapping("stock/stockvalue")
public class StockValueController {
    @Autowired
    private StockValueService stockValueService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = stockValueService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		StockValueEntity stockValue = stockValueService.getById(id);

        return R.ok().put("stockValue", stockValue);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody StockValueEntity stockValue){
		stockValueService.save(stockValue);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody StockValueEntity stockValue){
		stockValueService.updateById(stockValue);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		stockValueService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
