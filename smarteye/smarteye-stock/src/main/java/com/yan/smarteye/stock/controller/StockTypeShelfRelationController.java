package com.yan.smarteye.stock.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.yan.smarteye.stock.entity.ShelfEntity;
import com.yan.smarteye.stock.entity.SupplierEntity;
import com.yan.smarteye.stock.service.WareLocationSupplierRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yan.smarteye.stock.entity.StockTypeShelfRelationEntity;
import com.yan.smarteye.stock.service.StockTypeShelfRelationService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 库存种类&货架关联
 */
@RestController
@RequestMapping("stock/stocktypeshelfrelation")
public class StockTypeShelfRelationController {
    @Autowired
    private StockTypeShelfRelationService stockTypeShelfRelationService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = stockTypeShelfRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		StockTypeShelfRelationEntity stockTypeShelfRelation = stockTypeShelfRelationService.getById(id);

        return R.ok().put("stockTypeShelfRelation", stockTypeShelfRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody StockTypeShelfRelationEntity stockTypeShelfRelation){
		stockTypeShelfRelationService.save(stockTypeShelfRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody StockTypeShelfRelationEntity stockTypeShelfRelation){
		stockTypeShelfRelationService.updateById(stockTypeShelfRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		stockTypeShelfRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
