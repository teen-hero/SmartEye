package com.yan.common.to;

import lombok.Data;

import java.util.Date;
@Data
public class WareMaterialRespTo {

    private String wlName;
    private String wlpName;
    private String wlppName;


    /**
     * 库存品id
     */
    private Long warematerialId;
    /**
     * 库存种类值
     */
    private String valueSelect;
    /**
     * 库存种类名称
     */
    private String materialTypeName;
    /**
     * 所属分区id
     */
    private Long wlId;
    /**
     * 货架名称
     */
    private String shelfName;
    /**
     * 真实库存
     */
    private Integer realCount;
    /**
     * 预警库存
     */
    private Integer warnCount;
    /**
     *
     */
    private Date updateTime;
    /**
     * 员工等级总体把握
     */
    private Integer levelNumb;
    /**
     * 车间id
     */
    private Long wlpId;
    /**
     * 厂区id
     */
    private Long wlppId;
}
