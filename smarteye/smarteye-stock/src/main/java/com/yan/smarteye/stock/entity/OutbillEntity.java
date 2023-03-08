package com.yan.smarteye.stock.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 出库单

 */
@Data
@TableName("gms_outbill")
public class OutbillEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long outbillId;
	/**
	 * 申请单id
	 */
	private Long mbillId;
	/**
	 * 出库单名
	 */
	private String outbillName;
	/**
	 * 出库状态[0 - 新建，1 - 完成出库]
	 */
	private Integer status;
	/**
	 * 出库类型[1，物料申请单出库 2，手动出库 3，其他出库]
	 */
	private Integer outtype;
	/**
	 * 描述
	 */
	private String outbillDescription;
	/**
	 * 所属分区id
	 */
	private Long wlId;
	/**
	 * 
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

}
