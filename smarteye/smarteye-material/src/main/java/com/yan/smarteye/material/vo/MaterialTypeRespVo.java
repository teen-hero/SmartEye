package com.yan.smarteye.material.vo;

import lombok.Data;

//供前端响应使用
@Data
public class MaterialTypeRespVo extends MaterialTypeShelfVo {

    private String wlName;
    private String shelfName;

    private Long[] warelocationPath;
}
