package com.yan.smarteye.stock.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 库存分区&供用商关联
 *
 */
@Data
@TableName("gms_ware_location_supplier_relation")
public class WareLocationSupplierRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 货架id
	 */
	private Long supplierId;
	/**
	 * 分区id
	 */
	private Long wlId;
	/**
	 * 
	 */
	private String supplierName;
	/**
	 * 
	 */
	private String wlName;

}
