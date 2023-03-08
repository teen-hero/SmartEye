package com.yan.smarteye.material.vo.savembillvo;

import lombok.Data;

import java.util.List;

@Data
public class MbillVo {

    private String mbillName;

    private String mbillDescription;

    private Long wlId;

    private List<MbilldetialVo> mbilldetialVos;
}
