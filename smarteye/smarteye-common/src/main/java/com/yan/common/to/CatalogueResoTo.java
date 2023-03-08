package com.yan.common.to;

import lombok.Data;

@Data
public class CatalogueResoTo {
    private String name;
    private Long id;
    private CatalogueResoTo catalogueResoVo;
}
