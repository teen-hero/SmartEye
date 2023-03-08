package com.yan.smarteye.stock.vo;

import com.yan.smarteye.stock.entity.ShelfEntity;
import com.yan.smarteye.stock.entity.StockTypeEntity;
import lombok.Data;

import java.util.List;

@Data
public class ShelfWithStockTypeVo extends ShelfEntity {
    private Long wlId;

    private List<StockTypeEntity> stockTypeEntities;
}
