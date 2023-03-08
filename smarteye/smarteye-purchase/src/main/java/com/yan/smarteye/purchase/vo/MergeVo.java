package com.yan.smarteye.purchase.vo;

import lombok.Data;

import java.util.List;

@Data
public class MergeVo {
    private Long buyListId;   //采购单id
    private List<Long> items;   //合并项集合(buyDemandId的集合)
}
