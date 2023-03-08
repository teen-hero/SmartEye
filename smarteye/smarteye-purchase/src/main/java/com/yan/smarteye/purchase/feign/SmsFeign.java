package com.yan.smarteye.purchase.feign;

import com.yan.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient("smarteye-third")
public interface SmsFeign {
    /**
     * 发送短信内容，预警库存
     */
    @GetMapping("/sms/sendwarninfo")
    @ResponseBody
    public R sendWarnInfo(@RequestParam("phones") List<String> phones, @RequestParam("msg") String msg);
}
