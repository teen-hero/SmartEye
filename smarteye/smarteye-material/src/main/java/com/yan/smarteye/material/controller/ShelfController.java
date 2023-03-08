package com.yan.smarteye.material.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.yan.smarteye.material.entity.MaterialTypeEntity;
import com.yan.smarteye.material.service.MaterialTypeService;
import com.yan.smarteye.material.service.MaterialTypeShelfRelationService;
import com.yan.smarteye.material.service.WareLocationService;
import com.yan.smarteye.material.vo.ShelfRelationVo;
import com.yan.smarteye.material.vo.ShelfWithMaterialTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yan.smarteye.material.entity.ShelfEntity;
import com.yan.smarteye.material.service.ShelfService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 货架
 */
@RestController
@RequestMapping("material/shelf")
public class ShelfController {
    @Autowired
    private ShelfService shelfService;
    @Autowired
    WareLocationService wareLocationService;
    @Autowired
    MaterialTypeService materialTypeService;
    @Autowired
    MaterialTypeShelfRelationService materialTypeShelfRelationService;


    //查询三级目录wlId下的属性数据并分页查询，并支持查找（如果参数中有就查找，没有就不查），0查全部
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
        //根据attrGroupId查出对应的实体类
        ShelfEntity shelf = shelfService.getById(shelfId);
        //从实体类中获取catelogId
        Long wlId = shelf.getWlId();
        //根据catelogId调用categoryService 查出 路径
        Long[] path = wareLocationService.findCatelogPath(wlId);
        //把路径设置到其中 返回给前端
        shelf.setWarelocationPath(path);
        return R.ok().put("data", shelf);
    }

    /**
     * 获取货架关联的所有库存种类
     */
    @GetMapping("/{shelfId}/materialType/relation")
    public R materialTypeRelation(@PathVariable("shelfId") Long shelfId){
        List<MaterialTypeEntity> entities =  materialTypeService.getRelationMaterialType(shelfId);
        return R.ok().put("data",entities);
    }


    /**
     * 获取货架未关联的所有库存种类
     */
    @GetMapping("/{shelfId}/materialType/norelation")
    public R materialTypeNoRelation(@PathVariable("shelfId") Long shelfId,
                            @RequestParam Map<String, Object> params){  //Map<String, Object> params就是个分页信息
        PageUtils page = materialTypeService.getNoRelationMaterialType(params,shelfId);
        return R.ok().put("page",page);
    }

    /**
     * 删除库存种类与货架的关联关系
     */
    @PostMapping("/materialType/relation/delete")
    //接收一个我们自定义的vo数组
    public R deleteRelation(@RequestBody  ShelfRelationVo[] vos){
        materialTypeService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 添加库存种类与货架的关联关系
     */
    @PostMapping("/materialType/relation")
    public R addRelation(@RequestBody List<ShelfRelationVo> vos){
        //重载的方法
        materialTypeShelfRelationService.saveBatch(vos);
        return R.ok();
    }

    //根据分类id查出所有的分组以及这些分组里面的属性
    @GetMapping("/{wlId}/withMaterialType")
    public R getShelfWithMaterialType(@PathVariable("wlId")Long wlId){
        //根据分类id查出所有的分组以及这些分组里面的属性
        List<ShelfWithMaterialTypeVo> vos =  shelfService.getShelfWithMaterialTypeBywlId(wlId);
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
