package com.yan.smarteye.material.vo.saveVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class MaterialVo {

    private String materialName;

    private String materialDescription;

    private Long wlId;

    private Long supplierId;

    private BigDecimal weight;

    private List<String> materialImageInfo;

    private List<OneMaterialVo> oneMaterial;
}