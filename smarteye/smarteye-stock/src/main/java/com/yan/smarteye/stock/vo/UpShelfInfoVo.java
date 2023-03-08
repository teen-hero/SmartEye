package com.yan.smarteye.stock.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UpShelfInfoVo {

    String shelfName;
    int qualityPeriod;
    int onestockCount;
    String valueSelect;
    Date updateTime;
    Long stockId;

}
