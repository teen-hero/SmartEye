package com.yan.smarteye.integration.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 员工表
 */
@Data
@TableName("ims_employee")
public class EmployeeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long eeId;
	/**
	 * 用户名
	 */
	private String eeName;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;
	/**
	 * 叫料权限等级
	 */
	private Integer levelNumb;
	/**
	 * 所属部门编号
	 */
	private Long deptId;
	/**
	 * 职能类型
	 */
	private String jobType;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

}
