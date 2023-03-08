package com.yan.smarteye.integration.service.impl;

import com.yan.smarteye.integration.service.MobileService;
import org.springframework.stereotype.Service;

@Service
public class MobileServiceImpl implements MobileService {

    @Override
    public int login(String username, String password, Integer loginType) {
        if(loginType!=null && loginType == 1){ //登陆方式：密码登录
            //查询数据库...暂时简单模拟实现
            if("admin".equals(username) && "admin".equals(password)){
                return 0;
            }
        }
        return 1;
    }
}
