package com.yan.smarteye.material.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 货架
 *
 */
@Data
@TableName("mms_shelf")
public class ShelfEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 货架id
	 */
	@TableId
	private Long shelfId;
	/**
	 * 货架名
	 */
	private String shelfName;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 描述
	 */
	private String descript;
	/**
	 * 货架图标
	 */
	private String icon;
	/**
	 * 所属分区id
	 */
	private Long wlId;

	/**
	 *  所属分组的路径[1,15,136] ，修改组时候的回显需要的数据
	 *  一个三级目录对应的完整路径 [2,25,225]  /A厂区/装配车间/反转区
	 */
	@TableField(exist = false)
	private Long[] warelocationPath;


}
