package com.yan.smarteye.stock.vo;

import lombok.Data;

//供前端响应使用
@Data
public class StockTypeRespVo extends StockTypeShelfVo {
    /**
     * 			"catelogName": "手机/数码/手机", //所属分类名字
     * 			"groupName": "主体", //所属分组名字
     */
    private String wlName;
    private String shelfName;

    private Long[] warelocationPath;
}
