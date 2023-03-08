package com.yan.smarteye.third.controller;

import com.yan.common.utils.R;
import com.yan.smarteye.third.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("third/oss")
public class OssController {

    @Autowired
    OssService ossService;

    //签名
    @RequestMapping("/policy")
    public R policy(){
        Map<String, String> policy = ossService.policy();
        return R.ok().put("data",policy);
    }

}
