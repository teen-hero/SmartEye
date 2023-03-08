package com.yan.smarteye.stock.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yan.common.exception.BizCodeEnume;
import com.yan.smarteye.stock.dao.StockTypeShelfRelationDao;
import com.yan.smarteye.stock.entity.StockTypeEntity;
import com.yan.smarteye.stock.service.StockTypeService;
import com.yan.smarteye.stock.service.StockTypeShelfRelationService;
import com.yan.smarteye.stock.service.WareLocationService;
import com.yan.smarteye.stock.vo.ShelfRelationVo;
import com.yan.smarteye.stock.vo.ShelfWithStockTypeVo;
import com.yan.smarteye.stock.vo.UpShelfInfoResp;
import com.yan.smarteye.stock.vo.UpShelfInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yan.smarteye.stock.entity.ShelfEntity;
import com.yan.smarteye.stock.service.ShelfService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 货架
 */
@RestController
@RequestMapping("stock/shelf")
public class ShelfController {
    @Autowired
    private ShelfService shelfService;
    @Autowired
    WareLocationService wareLocationService;
    @Autowired
    StockTypeService stockTypeService;
    @Autowired
    StockTypeShelfRelationService stockTypeShelfRelationService;


    //查询wlId下的货架的上架情况（onestock）详情，指的是某货架上放了什么
    @RequestMapping("/list/upshelfinfo")
    public R listshelfup(@RequestParam("wlId") Long wlId,@RequestParam("shelfName") String shelfName){
        List<UpShelfInfoVo> res = shelfService.queryUpshelfInfo(wlId,shelfName);
        if(res.size()==0){
            //该货架为空
            return R.error(BizCodeEnume.SHELF_EMPTY.getCode(),BizCodeEnume.SHELF_EMPTY.getMsg());
        }
        return R.ok().put("data",res);
    }

    //查询wlId下的货架的上架情况（onestock），指的是货架的使用率
    @RequestMapping("/list/{wlId}/shelfup")
    public R listshelfup(@PathVariable("wlId") Long wlId){
        List<UpShelfInfoResp> res = shelfService.queryUpshelf(wlId);
        return R.ok().put("data",res);
    }


    //查询三级目录wlId下的货架并分页查询，并支持查找（如果参数中有就查找，没有就不查），0查全部
    @RequestMapping("/list/{wlId}")
    public R listCatelogId(@RequestParam Map<String, Object> params,@PathVariable("wlId") Long wlId){
        PageUtils page = shelfService.queryPage(params,wlId);
        return R.ok().put("page", page);
    }

    /**
     * 信息,(回显修改信息)
     */
    @RequestMapping("/info/{shelfId}")
    public R info(@PathVariable("shelfId") Long shelfId){
        //根据sheflId查出对应的实体类
        ShelfEntity shelf = shelfService.getById(shelfId);
        //从实体类中获取wlId
        Long wlId = shelf.getWlId();
        //根据wlId调用wlService 查出 路径
        Long[] path = wareLocationService.findCatelogPath(wlId);
        //把路径设置到其中 返回给前端
        shelf.setWarelocationPath(path);
        return R.ok().put("data", shelf);
    }

    /**
     * 获取货架关联的所有库存种类
     */
    @GetMapping("/{shelfId}/stockType/relation")
    public R stockTypeRelation(@PathVariable("shelfId") Long shelfId){
        List<StockTypeEntity> entities =  stockTypeService.getRelationStockType(shelfId);
        return R.ok().put("data",entities);
    }


    /**
     * 获取货架未关联的所有库存种类
     */
    @GetMapping("/{shelfId}/stockType/norelation")
    public R stockTypeNoRelation(@PathVariable("shelfId") Long shelfId,
                            @RequestParam Map<String, Object> params){
        PageUtils page = stockTypeService.getNoRelationStockType(params,shelfId);
        return R.ok().put("page",page);
    }

    /**
     * 删除库存种类与货架的关联关系
     */
    @PostMapping("/stockType/relation/delete")
    //接收一个我们自定义的vo数组
    public R deleteRelation(@RequestBody  ShelfRelationVo[] vos){
        stockTypeService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 添加库存种类与货架的关联关系
     */
    @PostMapping("/stockType/relation")
    public R addRelation(@RequestBody List<ShelfRelationVo> vos){
        //重载的方法
        stockTypeShelfRelationService.saveBatch(vos);
        return R.ok();
    }

    //根据分类id查出所有的分组以及这些分组里面的属性
    @GetMapping("/{wlId}/withStockType")
    public R getShelfWithStockType(@PathVariable("wlId")Long wlId){
        //根据分类id查出所有的分组以及这些分组里面的属性
        List<ShelfWithStockTypeVo> vos =  shelfService.getShelfWithStockTypeBywlId(wlId);
        return R.ok().put("data",vos);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = shelfService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ShelfEntity shelf){
		shelfService.save(shelf);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ShelfEntity shelf){
		shelfService.updateById(shelf);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] shelfIds){
		shelfService.removeByIds(Arrays.asList(shelfIds));

        return R.ok();
    }

}
