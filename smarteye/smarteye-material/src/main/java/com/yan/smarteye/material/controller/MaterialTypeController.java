package com.yan.smarteye.material.controller;

import java.util.Arrays;
import java.util.Map;

import com.yan.smarteye.material.service.ShelfService;
import com.yan.smarteye.material.vo.MaterialTypeRespVo;
import com.yan.smarteye.material.vo.MaterialTypeShelfVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yan.smarteye.material.service.MaterialTypeService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 库存种类
 *
 */
@RestController
@RequestMapping("material/materialtype")
public class MaterialTypeController {
    @Autowired
    private MaterialTypeService materialTypeService;
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
        PageUtils page = materialTypeService.queryBaseMaterialTypePage(params,wlId,type);
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
    public R save(@RequestBody MaterialTypeShelfVo materialTypeShelfVo){
        materialTypeService.saveDetial(materialTypeShelfVo);

        return R.ok();
    }


    /**
     * 根据id查询，回显
     */
    @RequestMapping("/info/{materialTypeId}")
    public R info(@PathVariable("materialTypeId") Long materialTypeId){
        //查询详情 返回带有详细信息的respVo对象
        MaterialTypeRespVo materialTypeRespVo= materialTypeService.getMaterialTypeInfo(materialTypeId);
        return R.ok().put("data", materialTypeRespVo);
    }

    /**
     * 修改,修改库存种类信息 及 对应的库存与货架关系
     */
    @RequestMapping("/update")
    public R update(@RequestBody MaterialTypeShelfVo materialTypeShelfVo){
        materialTypeService.updateDetial(materialTypeShelfVo);

        return R.ok();
    }

    /**
     * 删除
     * TODO 改为删除库存种类 及 与货架的关系
     */

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] materialTypeIds){
        materialTypeService.removeByIds(Arrays.asList(materialTypeIds));

        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = materialTypeService.queryPage(params);

        return R.ok().put("page", page);
    }






}
