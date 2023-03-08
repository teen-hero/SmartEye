package com.yan.common.to;

import lombok.Data;

@Data
public class LowWarnCountTo {

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
