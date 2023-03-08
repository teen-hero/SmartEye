package com.yan.auth.feign;

import com.yan.auth.vo.UserLoginVo;
import com.yan.auth.vo.UserRegistVo;
import com.yan.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("smarteye-integration")
public interface IntegrationFeignService {
    //保存注册的用户
    @RequestMapping("integration/employee/register")
    public R regist(@RequestBody UserRegistVo registerVo);

    //普通登录
    @RequestMapping("integration/employee/login")
    public R login(@RequestBody UserLoginVo loginVo);
}
