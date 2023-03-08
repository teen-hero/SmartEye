package com.yan.smarteye.purchase.vo;

import lombok.Data;

//返回用于发送短信提醒预警库存
@Data
public class WarnEntityInfoVo {

    private Long warestockId;

    private String valueSelect;
    /**
     * 所属分区id
     */
    private Long wlId;
    /**
     * 货架名称
     */
    private String shelfName;
}
