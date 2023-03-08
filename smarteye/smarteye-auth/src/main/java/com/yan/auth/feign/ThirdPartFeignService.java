package com.yan.auth.feign;

import com.yan.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("smarteye-third")
public interface ThirdPartFeignService {

    @GetMapping("/sms/sendcode")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
