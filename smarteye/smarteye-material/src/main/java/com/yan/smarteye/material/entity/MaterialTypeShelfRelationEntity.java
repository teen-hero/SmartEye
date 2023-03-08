package com.yan.smarteye.material.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 库存种类&货架关联
 */
@Data
@TableName("mms_material_type_shelf_relation")
public class MaterialTypeShelfRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 库存种类id
	 */
	private Long materialTypeId;
	/**
	 * 货架id
	 */
	private Long shelfId;
	/**
	 * 库存种类组内排序
	 */
	private Integer materialTypeSort;

}
