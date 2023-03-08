package com.yan.smarteye.material.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 物料申请单详情
 */
@Data
@TableName("mms_mbilldetail")
public class MbilldetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 申请单详情id
	 */
	@TableId
	private Long mbilldetailId;
	/**
	 * 申请单id
	 */
	private Long mbillId;
	/**
	 * 描述
	 */
	private String mbilldetailDescription;
	/**
	 * 所属分区id
	 */
	private Long wlId;
	/**
	 * 所属货架名
	 */
	private String shelfName;
	/**
	 * 数量
	 */
	private Integer mbilldetailCount;
	/**
	 * 状态[0 - 申请中，1 - 已送达]
	 */
	private Integer status;
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
	/**
	 * 货物种类值
	 */
	private String valueSelect;
	/**
	 * 库存种类id
	 */
	private Long materialTypeId;
	/**
	 * 车间id
	 */
	private Long wlpId;
	/**
	 * 厂区id
	 */
	private Long wlppId;

	private Integer levelNumb;

}
