package com.yan.common.to;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//供远程调用封装参数
@Data
public class WareStockTo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 货物id
     */
    private Long onestockId;

    /**
     * 库存种类值
     */
    private String valueSelect;
    /**
     * 库存种类名称
     */
    private String stockTypeName;
    /**
     * 所属分区id
     */
    private Long wlId;
    /**
     * 货架名称
     */
    private String shelfName;
    /**
     * onestock数量
     */
    private Integer onestockCount;
    /**
     * 车间id
     */
    private Long wlpId;
    /**
     * 厂区id
     */
    private Long wlppId;


}
