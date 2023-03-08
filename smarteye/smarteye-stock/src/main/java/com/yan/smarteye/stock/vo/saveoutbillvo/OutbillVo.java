package com.yan.smarteye.stock.vo.saveoutbillvo;

import lombok.Data;

import java.util.List;

@Data
public class OutbillVo {
    private String outbillName;
    private String outbillDescription;
    private Integer outtype;
    private Long wlId;
    private Long mbillId;

    private String miToken;

    private List<OutbilldetailVo> outbilldetialVos;

}


