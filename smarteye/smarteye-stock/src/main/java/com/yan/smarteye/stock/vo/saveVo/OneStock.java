package com.yan.smarteye.stock.vo.saveVo;

import lombok.Data;

@Data
public class OneStock {

    private Long stockTypeId;

    private String valueSelect;

    private Integer onestockCount;

    private Integer levelNumb;  //员工等级 TODO 后期换成具体员工的选项代替（如操作工、库存管理员等职业选择），更加方便用户使用

    private Integer qualityPeriod;

    private String shelfName;
}