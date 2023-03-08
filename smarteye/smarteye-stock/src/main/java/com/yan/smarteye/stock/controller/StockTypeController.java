package com.yan.smarteye.stock.controller;

import java.util.Arrays;
import java.util.Map;


import com.yan.smarteye.stock.entity.ShelfEntity;
import com.yan.smarteye.stock.service.ShelfService;
import com.yan.smarteye.stock.vo.StockTypeRespVo;
import com.yan.smarteye.stock.vo.StockTypeShelfVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yan.smarteye.stock.entity.StockTypeEntity;
import com.yan.smarteye.stock.service.StockTypeService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 库存种类
 */
@RestController
@RequestMapping("stock/stocktype")
public class StockTypeController {
    @Autowired
    private StockTypeService stockTypeService;
    @Autowired
    ShelfService shelfService;


    /**
     * 列表,查询某三级目录分类目录下的库存种类，0为全部
     * attrType暂为base、sale
     */
    @GetMapping("/{attrType}/list/{wlId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("wlId") Long wlId,
                          @PathVariable("attrType")String type){

        //1 把传入的type 参数列表 要查询哪个目录下的目录ID 用其调用service层方法
        PageUtils page = stockTypeService.queryBaseStockTypePage(params,wlId,type);
        return R.ok().put("page", page);
    }


    //查询三级目录wlId下的货架数据并分页查询，并支持查找（如果参数中有就查找，没有就不查），0查全部
    @RequestMapping("/list/{wlId}")
    public R listwlId(@RequestParam Map<String, Object> params,@PathVariable("wlId") Long wlId){
        PageUtils page = shelfService.queryPage(params,wlId);
        return R.ok().put("page", page);
    }

    /**
     * 保存库存种类信息 及 对应的库存与货架关系
     */
    @RequestMapping("/save")
    public R save(@RequestBody StockTypeShelfVo stockTypeShelfVo){
        stockTypeService.saveDetial(stockTypeShelfVo);

        return R.ok();
    }


    /**
     * 根据id查询，回显
     */
    @RequestMapping("/info/{stockTypeId}")
    public R info(@PathVariable("stockTypeId") Long stockTypeId){
        //查询详情 返回带有详细信息的respVo对象
        StockTypeRespVo stockTypeRespVo= stockTypeService.getStockTypeInfo(stockTypeId);
        return R.ok().put("data", stockTypeRespVo);
    }

    /**
     * 修改,修改库存种类信息 及 对应的库存与货架关系
     */
    @RequestMapping("/update")
    public R update(@RequestBody StockTypeShelfVo stockTypeShelfVo){
        stockTypeService.updateDetial(stockTypeShelfVo);

        return R.ok();
    }

    /**
     * 删除
     * TODO 改为删除库存种类 及 与货架的关系
     */

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] stockTypeIds){
        stockTypeService.removeByIds(Arrays.asList(stockTypeIds));

        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = stockTypeService.queryPage(params);

        return R.ok().put("page", page);
    }






}
