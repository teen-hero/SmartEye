package com.yan.smarteye.purchase.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yan.common.to.EmployeeEntityTo;
import com.yan.common.utils.ChangeList;
import com.yan.smarteye.purchase.feign.EmployeeFeign;
import com.yan.smarteye.purchase.feign.SmsFeign;
import com.yan.smarteye.purchase.vo.MergeVo;
import com.yan.smarteye.purchase.vo.WarnEntityInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yan.smarteye.purchase.entity.BuyDemandEntity;
import com.yan.smarteye.purchase.service.BuyDemandService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 采购需求
 */
@Slf4j
@RestController
@RequestMapping("purchase/buydemand")
public class BuyDemandController {
    @Autowired
    private BuyDemandService buyDemandService;
    @Autowired
    EmployeeFeign employeeFeign;
    @Autowired
    SmsFeign smsFeign;

    //合并采购需求（就是添加到采购单即添加采购需求表中的采购单id）
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo){
        //合并的同时，返回未合并成功的采购需求id给前端
        List<Long> unMergeIds = buyDemandService.mergePurchase(mergeVo);
        //这个判断应该加在前端，但是我不会前端
        if (unMergeIds.size()==0){
            unMergeIds = null;
        }
        return R.ok().put("data",unMergeIds);
    }


    /**
     * TODO 目前是刷新采购需求页面触发检测，后期改为定时任务，无需点击采购需求页面也能触发预警检测
     * 自动检测预警库存，生成采购需求
     */
    @RequestMapping("/autodemand")
    public R autodemand(){

        //1.预警库存的货物，生成采购需求
        List<WarnEntityInfoVo> warnInfos = buyDemandService.autoRemand();
        //若无预警发生，无需进行下面发送短信提醒步骤，直接返回
        if(warnInfos.size()==0){
            return R.ok();
        }
        //2.发送短信告知管理员产生了采购需求（该短信发给采购管理员、库存管理员，但目前只发给库存管理员）
        //1.短信内容msg
        String msg = "【SmartEye库存预警】";
        for(WarnEntityInfoVo info : warnInfos){
            msg = msg + "分区：["+info.getWlId()+"]货架：["+info.getShelfName()
                    +"]货物种类：【"+info.getValueSelect()+"】库存已低于预警值";
        }
        log.info(msg);
        //2.发送的手机号
        //TODO 参数"库存管理员"写死代码不合适，改为一个常量输入
        R r = employeeFeign.getOneTypeEE("库存管理员");
        Object data = r.get("data");
        List<EmployeeEntityTo> tos = ChangeList.changeList(data, EmployeeEntityTo.class);
        List<String> phones = new ArrayList<>();
        for(EmployeeEntityTo to : tos){
            String phone = to.getMobile();
            phones.add(phone);
        }
        //发送短信
        R smsRes = smsFeign.sendWarnInfo(phones, msg);
        //若存在部分手机号未发生成功，则将未发送成功的手机号重新发送一次
        if(smsRes.getCode()!=0){
            Object smsData = smsRes.get("data");
            List<String> unSends = ChangeList.changeList(smsData, String.class);
            smsFeign.sendWarnInfo(unSends,msg);
        }
        return R.ok();
    }



    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = buyDemandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{buyDemandId}")
    public R info(@PathVariable("buyDemandId") Long buyDemandId){
		BuyDemandEntity buyDemand = buyDemandService.getById(buyDemandId);

        return R.ok().put("data", buyDemand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody BuyDemandEntity buyDemand){
		buyDemandService.save(buyDemand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BuyDemandEntity buyDemand){
		buyDemandService.updateById(buyDemand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] buyDemandIds){
		buyDemandService.removeByIds(Arrays.asList(buyDemandIds));

        return R.ok();
    }

}
