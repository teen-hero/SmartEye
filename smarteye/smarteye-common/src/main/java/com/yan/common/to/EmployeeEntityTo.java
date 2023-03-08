package com.yan.common.to;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
@Data
public class EmployeeEntityTo {

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
    private Date createTime;
}
