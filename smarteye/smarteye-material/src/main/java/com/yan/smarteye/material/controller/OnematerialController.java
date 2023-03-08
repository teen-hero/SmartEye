package com.yan.smarteye.material.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yan.smarteye.material.entity.OnematerialEntity;
import com.yan.smarteye.material.entity.ShelfEntity;
import com.yan.smarteye.material.entity.MaterialTypeShelfRelationEntity;
import com.yan.smarteye.material.service.ShelfService;
import com.yan.smarteye.material.service.MaterialTypeShelfRelationService;
import com.yan.smarteye.material.vo.OneMaterialRespVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.smarteye.material.service.OnematerialService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 待上架或已上架的一种货物
 */
@RestController
@RequestMapping("material/onematerial")
public class OnematerialController {
    @Autowired
    private OnematerialService onematerialService;
    @Autowired
    MaterialTypeShelfRelationService materialTypeShelfRelationService;
    @Autowired
    ShelfService shelfService;

    /**
     * 查询materialid下的所有详情
     */
    @RequestMapping("{materialId}/info")
    public R millinfo(@PathVariable("materialId") Long materialId){
        List<OnematerialEntity> res = onematerialService.getByMaterialId(materialId);

        return R.ok().put("data", res);
    }

    /**
     * 过渡方法,处理前端数据再返会前端
     */
    @RequestMapping("/oneinfo")
    public R info(@RequestBody List<OneMaterialRespVo> vos){
        List<OneMaterialRespVo> voList = new ArrayList<>();
        for (OneMaterialRespVo vo : vos){
            if (vo.getValueSelect()!=null && !vo.getValueSelect().equals("")){
                MaterialTypeShelfRelationEntity relationEntity = materialTypeShelfRelationService.getOne(new QueryWrapper<MaterialTypeShelfRelationEntity>().eq("material_type_id", vo.getMaterialTypeId()));
                ShelfEntity shelfEntity = shelfService.getOne(new QueryWrapper<ShelfEntity>().eq("shelf_id", relationEntity.getShelfId()));
                OneMaterialRespVo oneMaterialRespVo = new OneMaterialRespVo();
                BeanUtils.copyProperties(vo,oneMaterialRespVo);
                oneMaterialRespVo.setShelfName(shelfEntity.getShelfName());
                voList.add(oneMaterialRespVo);
            }
        }
        return R.ok().put("data", voList);
    }

    /**
     * 列表,检索查询
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = onematerialService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }

    /**
     * 上架onematerial，并向wms中添加warematerial库存
     */
    @RequestMapping("/{onematerialId}/up")
    public R up(@PathVariable("onematerialId") Long onematerialId){
        onematerialService.up(onematerialId);

        return R.ok();
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{onematerialId}")
    public R info(@PathVariable("onematerialId") Long onematerialId){
		OnematerialEntity onematerial = onematerialService.getById(onematerialId);

        return R.ok().put("onematerial", onematerial);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OnematerialEntity onematerial){
		onematerialService.save(onematerial);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OnematerialEntity onematerial){
		onematerialService.updateById(onematerial);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] onematerialIds){
		onematerialService.removeByIds(Arrays.asList(onematerialIds));

        return R.ok();
    }

}
