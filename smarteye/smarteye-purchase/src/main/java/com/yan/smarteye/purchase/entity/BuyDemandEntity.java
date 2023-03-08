package com.yan.smarteye.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 采购需求
 */
@Data
@TableName("pms_buy_demand")
public class BuyDemandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long buyDemandId;
	/**
	 * 采购单id
	 */
	private Long buyListId;
	/**
	 * 采购货物id
	 */
	private Long warestockId;
	/**
	 * 采购数量
	 */
	private Integer buyNum;
	/**
	 * 状态[0新建，1已分配，2正在采购，3已完成，4采购失败]
	 */
	private Integer status;
	/**
	 * 需求来源[0手动添加，1库存预警]
	 */
	private Integer origin;
	/**
	 * 库存种类值
	 */
	private String valueSelect;

}
