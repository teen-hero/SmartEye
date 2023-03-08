package com.yan.smarteye.stock.vo;

import com.yan.smarteye.stock.entity.StockTypeEntity;
import lombok.Data;

@Data
public class StockTypeShelfVo extends StockTypeEntity {
    private Long shelfId;
}
