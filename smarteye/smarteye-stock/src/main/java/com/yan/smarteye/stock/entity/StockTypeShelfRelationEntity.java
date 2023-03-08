package com.yan.smarteye.stock.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 库存种类&货架关联

 */
@Data
@TableName("gms_stock_type_shelf_relation")
public class StockTypeShelfRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 库存种类id
	 */
	private Long stockTypeId;
	/**
	 * 货架id
	 */
	private Long shelfId;
	/**
	 * 库存种类组内排序
	 */
	private Integer stockTypeSort;

}
