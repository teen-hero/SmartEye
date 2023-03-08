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
@TableName("mms_askmbill")
public class AskmbillEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 叫料单id
	 */
	@TableId
	private Long askmbillId;
	/**
	 * 叫料人员id
	 */
	private Long eeId;
	/**
	 * 描述
	 */
	private String askmbillDescription;
	/**
	 * 叫料位置id
	 */
	private Long asklocationId;
	/**
	 * 叫料处理分区id
	 */
	private Long wlId;
	/**
	 * 叫料位置名
	 */
	private String asklocationName;
	/**
	 * 货物种类值
	 */
	private String valueSelect;
	/**
	 * 数量
	 */
	private Integer count;
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

}
