package com.yan.smarteye.stock.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 库存种类

 */
@Data
@TableName("gms_stock_type")
public class StockTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 库存种类id
	 */
	@TableId
	private Long stockTypeId;
	/**
	 * 库存种类名
	 */
	private String stockTypeName;
	/**
	 * 是否需要检索[0-不需要，1-需要]
	 */
	private Integer searchType;
	/**
	 * 库存种类图标
	 */
	private String icon;
	/**
	 * 可选值列表[用逗号分隔]
	 */
	private String valueSelect;
	/**
	 * 库存种类类型[0-库存外属性，1-库存属性
	 */
	private Integer stockTypeType;
	/**
	 * 启用状态[0 - 禁用，1 - 启用]
	 */
	private Long enable;
	/**
	 * 所属分区
	 */
	private Long wlId;
	/**
	 * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
	 */
	private Integer showDesc;

}
