package com.yan.smarteye.ware.vo;


import lombok.Data;

@Data
public class OutbilldetailVo {
    /**
     * id
     */
    private Long outbilldetailId;
    /**
     * 对应出库单id
     */
    private Long outbillId;
    /**
     * 出库状态[0 - 新建，1 - 完成出库]
     */
    private Integer status;
    /**
     * 所属分区id
     */
    private Long wlId;
    /**
     * 所属货架名
     */
    private String shelfName;
    /**
     * 数量
     */
    private Integer outbilldetailCount;
    /**
     * 货物种类值
     */
    private String valueSelect;
    /**
     * 库存种类id
     */
    private Long stockTypeId;
    /**
     * 车间id
     */
    private Long wlpId;
    /**
     * 厂区id
     */
    private Long wlppId;

}
