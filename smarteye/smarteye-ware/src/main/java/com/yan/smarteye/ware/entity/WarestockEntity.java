package com.yan.smarteye.ware.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 库存品
 */
@Data
@TableName("wms_warestock")
public class WarestockEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 库存品id
	 */
	@TableId
	private Long warestockId;
	/**
	 * 库存种类值
	 */
	private String valueSelect;
	/**
	 * 库存种类名称
	 */
	private String stockTypeName;
	/**
	 * 所属分区id
	 */
	private Long wlId;
	/**
	 * 货架名称
	 */
	private String shelfName;
	/**
	 * 真实库存
	 */
	private Integer realCount;
	/**
	 * 预警库存
	 */
	private Integer warnCount;
	/**
	 * 厂区id
	 */
	private Integer lockCount;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	/**
	 * 员工等级总体把握
	 */
	private Integer levelNumb;
	/**
	 * 车间id
	 */
	private Long wlpId;
	/**
	 * 厂区id
	 */
	private Long wlppId;

}
