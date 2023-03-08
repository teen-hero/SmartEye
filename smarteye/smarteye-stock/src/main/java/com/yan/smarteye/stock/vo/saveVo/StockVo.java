package com.yan.smarteye.stock.vo.saveVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class StockVo {

    private String stockName;

    private String stockDescription;

    private Long wlId;

    private Long supplierId;

    private BigDecimal weight;

    private List<String> stockImageInfo;

    private List<OneStock> oneStock;
}