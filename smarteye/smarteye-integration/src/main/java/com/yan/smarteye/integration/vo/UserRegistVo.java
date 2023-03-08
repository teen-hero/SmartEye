package com.yan.smarteye.integration.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 注册数据封装 Vo
 */
@Data
public class UserRegistVo implements Serializable {
    @NotEmpty(message = "用户名必须提交")
    @Length(min = 6,max = 10,message = "用户名必须是6-10位字符") //避免用户名与手机号重复导致后续登录判断出现两个用户
    private String userName;

    @NotEmpty(message = "密码必须填写")
    @Length(min = 6,max = 18,message = "密码必须是6-18位字符")
    private String password;

    @NotEmpty(message = "手机号码必须提交")
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$",message = "手机格式不正确")
    private String phone;

//    @NotEmpty(message = "验证码必须填写")
//    private String code;
}
