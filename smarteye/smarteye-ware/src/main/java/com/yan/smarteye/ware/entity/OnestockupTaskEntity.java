package com.yan.smarteye.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("wms_Onestockup_task")
public class OnestockupTaskEntity implements Serializable {

    @TableId
    Long id;

    Long onestockId;
    Long warestockId;
    Integer upNum;

    Integer status;
}
