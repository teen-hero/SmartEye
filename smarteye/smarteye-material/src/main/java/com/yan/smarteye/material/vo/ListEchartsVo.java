package com.yan.smarteye.material.vo;

import lombok.Data;

import java.util.List;

@Data
public class ListEchartsVo {
    String name;
    int value = 1;
    List<ListEchartsVo> children;
}
