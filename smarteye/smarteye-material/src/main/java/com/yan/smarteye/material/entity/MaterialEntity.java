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
 * 待上架或已上架的一批货物
 */
@Data
@TableName("mms_material")
public class MaterialEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 货物id
	 */
	@TableId
	private Long materialId;
	/**
	 * 货物名称
	 */
	private String materialName;
	/**
	 * 描述
	 */
	private String materialDescription;
	/**
	 * 描述图片[展示图片]
	 */
	private String materialImage;
	/**
	 * 所属分区id
	 */
	private Long wlId;
	/**
	 * 供用商id
	 */
	private Long supplierId;
	/**
	 * 保质期天数
	 */
	private Integer qualityPeriod;
	/**
	 * 
	 */
	private BigDecimal weight;
	/**
	 * 上架状态[0 - 下架，1 - 上架]
	 */
	private Integer publishStatus;
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
