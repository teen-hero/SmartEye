package com.yan.smarteye.ware.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yan.common.exception.BizCodeEnume;
import com.yan.common.to.CatalogueResoTo;
import com.yan.common.to.LowWarnCountTo;
import com.yan.common.to.WareMaterialTo;
import com.yan.common.to.WareStockTo;
import com.yan.smarteye.ware.dao.WarestockDao;
import com.yan.smarteye.ware.exception.NoStockException;
import com.yan.smarteye.ware.exception.NullWareStock;
import com.yan.smarteye.ware.vo.CatalogueVo;
import com.yan.smarteye.ware.vo.OutbilldetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.smarteye.ware.entity.WarestockEntity;
import com.yan.smarteye.ware.service.WarestockService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 库存品

 */
@RestController
@RequestMapping("ware/warestock")
public class WarestockController {
    @Autowired
    private WarestockService warestockService;
    @Autowired
    WarestockDao warestockDao;

    //测试
    @RequestMapping("/a")
    public R ad(){
        QueryWrapper<WarestockEntity> queryWrapper = new QueryWrapper<WarestockEntity>().groupBy("shelf_name");
        List<Map<String, Object>> maps = warestockDao.selectMaps(queryWrapper);
        for (Map map:maps){
            System.out.println(map);
        }

        return R.ok();
    }

    /**
     * 向wms中添加warestock库存
     */
    @RequestMapping("/add")
    public R add(@RequestBody WareStockTo wareStockTo){
        warestockService.add(wareStockTo);
        return R.ok();
    }


    /**
     * 列表,条件查询
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){

        PageUtils page = warestockService.queryPageByCondition(params);


        return R.ok().put("page", page);
    }

    /**
     * 返回低于预警库存的货物
     */
    @RequestMapping("/dangerCount")
    public R warnCount(@RequestParam Map<String, Object> params){
        PageUtils page = warestockService.getDangerCount(params);

        return R.ok().put("page", page);
    }

    /**
     * 返回低于预警库存的货物id、valueSelect
     */
    @RequestMapping("/warnCount")
    public R warnCount(){
        List<LowWarnCountTo> tos = warestockService.getLowWarnCount();

        return R.ok().put("data", tos);
    }

    /**
     * 生成出库单，立即锁定库存
     */
    @RequestMapping("/billout")
    public R billOut(@RequestBody List<OutbilldetailVo> vos){
        try {
            warestockService.billOut(vos);
        } catch (NoStockException e) {
            return R.error(BizCodeEnume.NOSTOCK_EXCEPTION.getCode(),BizCodeEnume.NOSTOCK_EXCEPTION.getMsg());
        } catch (NullWareStock nullWareStock) {
            return R.error(BizCodeEnume.NULLWARESTOCK_EXCEPTION.getCode(),BizCodeEnume.NULLWARESTOCK_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    /**
     * 出库显示库存余量
     */
    @RequestMapping("/count")
    public R count(@Param("wlId") Long wlId,@Param("shelfName") String shelfName,@Param("valueSelect") String valueSelect){
        //根据wlid、shelfname、valueslect确定唯一库存,并查出剩余库存量
        Integer count = warestockService.selectCount(wlId,shelfName,valueSelect);
        return R.ok().put("data",count);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{warestockId}")
    public R info(@PathVariable("warestockId") Long warestockId){
		WarestockEntity warestock = warestockService.getById(warestockId);

        return R.ok().put("data", warestock);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WarestockEntity warestock){
		warestockService.save(warestock);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WarestockEntity warestock){
		warestockService.updateById(warestock);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] warestockIds){
		warestockService.removeByIds(Arrays.asList(warestockIds));

        return R.ok();
    }

}
