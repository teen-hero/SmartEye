package com.yan.smarteye.stock.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 出库单详情
 *
 */
@Data
@TableName("gms_outbilldetail")
public class OutbilldetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long outbilldetailId;
	/**
	 * 对应出库单id
	 */
	private Long outbillId;
	/**
	 * 出库状态[0 - 新建，1 - 完成出库]
	 */
	private Integer status;
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
	private Integer outbilldetailCount;
	/**
	 * 货物种类值
	 */
	private String valueSelect;
	/**
	 * 库存种类id
	 */
	private Long stockTypeId;
	/**
	 * 车间id
	 */
	private Long wlpId;
	/**
	 * 厂区id
	 */
	private Long wlppId;

}
