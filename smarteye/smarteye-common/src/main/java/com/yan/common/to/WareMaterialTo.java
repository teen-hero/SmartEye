package com.yan.common.to;

import lombok.Data;

@Data
public class WareMaterialTo {

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
     * onestock数量
     */
    private Integer onematerialCount;
    /**
     * 车间id
     */
    private Long wlpId;
    /**
     * 厂区id
     */
    private Long wlppId;
}
