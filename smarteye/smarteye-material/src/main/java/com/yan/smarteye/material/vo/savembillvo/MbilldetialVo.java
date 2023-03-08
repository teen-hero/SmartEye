package com.yan.smarteye.material.vo.savembillvo;

import lombok.Data;

@Data
public class MbilldetialVo {

    private Long materialTypeId;

    private String valueSelect;

    private Integer mbilldetailCount;

    private Integer levelNumb;  //员工等级 TODO 后期换成具体员工的选项代替（如操作工、库存管理员等职业选择），更加方便用户使用

    private String shelfName;
}
