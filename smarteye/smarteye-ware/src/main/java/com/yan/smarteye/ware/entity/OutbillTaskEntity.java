package com.yan.smarteye.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("wms_outbill_task")
public class OutbillTaskEntity implements Serializable {

    @TableId
    Long id;

    Long wlId;

    String shelfName;

    String valueSelect;

    Integer lockNum;

    Long outbillId;

    Integer status;
}
