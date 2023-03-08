package com.yan.smarteye.material.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 待上架或已上架的一种货物
 */
@Data
@TableName("mms_onematerial")
public class OnematerialEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 货物id
	 */
	@TableId
	private Long onematerialId;
	/**
	 * 一批货物id
	 */
	private Long materialId;
	/**
	 * 描述
	 */
	private String onematerialDescription;
	/**
	 * 描述图片[展示图片]
	 */
	private String onematerialImage;
	/**
	 * 所属分区id
	 */
	private Long wlId;
	/**
	 * 所属货架名
	 */
	private String shelfName;
	/**
	 * 供用商id
	 */
	private Long supplierId;
	/**
	 * 保质期天数
	 */
	private Integer qualityPeriod;
	/**
	 * 数量
	 */
	private Integer onematerialCount;
	/**
	 * 
	 */
	private BigDecimal weight;
	/**
	 * 上架状态[0 - 下架，1 - 上架]
	 */
	private Integer publishStatus;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	/**
	 * 货物种类值
	 */
	private String valueSelect;
	/**
	 * 员工的等级
	 */
	private Integer levelNumb;
	/**
	 * 货物种类id
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

}
