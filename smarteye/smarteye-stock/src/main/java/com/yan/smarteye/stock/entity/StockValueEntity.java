package com.yan.smarteye.stock.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 货物的库存种类值

 */
@Data
@TableName("gms_stock_value")
public class StockValueEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 货物id
	 */
	private Long stockId;
	/**
	 * 库存种类id
	 */
	private Long stockTypeId;
	/**
	 * 库存种类名
	 */
	private String stockTypeName;
	/**
	 * 库存种类值
	 */
	private String stockTypeValue;
	/**
	 * 数量
	 */
	private Integer count;
	/**
	 * 顺序
	 */
	private Integer stockTypeSort;
	/**
	 * 快速展示【是否展示在介绍上；0-否 1-是】
	 */
	private Integer quickShow;

}
