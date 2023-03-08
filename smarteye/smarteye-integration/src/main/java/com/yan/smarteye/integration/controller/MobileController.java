package com.yan.smarteye.integration.controller;


import com.yan.common.utils.R;
import com.yan.smarteye.integration.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("integration/mobile")
public class MobileController {
    @Autowired
    MobileService mobileService;
    //登录
    @RequestMapping("/login/loginByPassword")
    public R login(@RequestBody Map<String ,Object> params){
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        Integer loginType = Integer.valueOf((String) params.get("loginType"));
        int res = mobileService.login(username,password,loginType);
        if (res==0){
            return R.ok().put("data",res).put("msg","登陆成功");
        }
        return R.error(400,"登陆失败");
    }
}
