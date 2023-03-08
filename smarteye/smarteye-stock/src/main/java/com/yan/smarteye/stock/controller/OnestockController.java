package com.yan.smarteye.stock.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yan.common.exception.BizCodeEnume;
import com.yan.smarteye.stock.entity.OnestockEntity;
import com.yan.smarteye.stock.entity.OutbilldetailEntity;
import com.yan.smarteye.stock.entity.ShelfEntity;
import com.yan.smarteye.stock.entity.StockTypeShelfRelationEntity;
import com.yan.smarteye.stock.service.ShelfService;
import com.yan.smarteye.stock.service.StockTypeShelfRelationService;
import com.yan.smarteye.stock.vo.OneStockRespVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.smarteye.stock.service.OnestockService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 待上架或已上架的一种货物
 */
@RestController
@RequestMapping("stock/onestock")
public class OnestockController {
    @Autowired
    private OnestockService onestockService;
    @Autowired
    StockTypeShelfRelationService stockTypeShelfRelationService;
    @Autowired
    ShelfService shelfService;


    /**
     * 查询stockid下的所有详情
     */
    @RequestMapping("{stockId}/info")
    public R millinfo(@PathVariable("stockId") Long stockId){
        List<OnestockEntity> res = onestockService.getByStockId(stockId);

        return R.ok().put("data", res);
    }

    /**
     * 过渡方法,处理前端数据再返会前端
     */
    @RequestMapping("/oneinfo")
    public R info(@RequestBody List<OneStockRespVo> vos){
        List<OneStockRespVo> voList = new ArrayList<>();
        for (OneStockRespVo vo : vos){
            if (vo.getValueSelect()!=null && !vo.getValueSelect().equals("")){
                StockTypeShelfRelationEntity relationEntity = stockTypeShelfRelationService.getOne(new QueryWrapper<StockTypeShelfRelationEntity>().eq("stock_type_id", vo.getStockTypeId()));
                ShelfEntity shelfEntity = shelfService.getOne(new QueryWrapper<ShelfEntity>().eq("shelf_id", relationEntity.getShelfId()));
                OneStockRespVo oneStockRespVo = new OneStockRespVo();
                BeanUtils.copyProperties(vo,oneStockRespVo);
                oneStockRespVo.setShelfName(shelfEntity.getShelfName());
                voList.add(oneStockRespVo);
            }
        }
        return R.ok().put("data", voList);
    }

    /**
     * 列表,检索查询
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = onestockService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }

    /**
     * 上架onestock，并向wms中添加warestock库存
     */
    @RequestMapping("/{onestockId}/up")
    public R up(@PathVariable("onestockId") Long onestockId){

        try {
            onestockService.up(onestockId);
        } catch (Exception exception) {
            return R.error(BizCodeEnume.ONESTOCKUP_FAIL.getCode(), BizCodeEnume.ONESTOCKUP_FAIL.getMsg());
        }

        return R.ok();
    }



    /**
     * 信息,远程调用
     */
    @RequestMapping("/info/{onestockId}")
    public R info(@PathVariable("onestockId") Long onestockId){
		OnestockEntity onestock = onestockService.getById(onestockId);

        return R.ok().put("data", onestock);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OnestockEntity onestock){
		onestockService.save(onestock);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OnestockEntity onestock){
		onestockService.updateById(onestock);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] onestockIds){
		onestockService.removeByIds(Arrays.asList(onestockIds));

        return R.ok();
    }

}
