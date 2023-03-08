package com.yan.smarteye.stock.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.smarteye.stock.entity.StockImagesEntity;
import com.yan.smarteye.stock.service.StockImagesService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 库存货物的图片集
 */
@RestController
@RequestMapping("stock/stockimages")
public class StockImagesController {
    @Autowired
    private StockImagesService stockImagesService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = stockImagesService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		StockImagesEntity stockImages = stockImagesService.getById(id);

        return R.ok().put("stockImages", stockImages);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody StockImagesEntity stockImages){
		stockImagesService.save(stockImages);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody StockImagesEntity stockImages){
		stockImagesService.updateById(stockImages);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		stockImagesService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
