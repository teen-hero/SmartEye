package com.yan.smarteye.material.vo;

import com.yan.smarteye.material.entity.ShelfEntity;
import com.yan.smarteye.material.entity.MaterialTypeEntity;
import lombok.Data;

import java.util.List;

@Data
public class ShelfWithMaterialTypeVo extends ShelfEntity {
    private Long wlId;

    private List<MaterialTypeEntity> materialTypeEntities;
}
