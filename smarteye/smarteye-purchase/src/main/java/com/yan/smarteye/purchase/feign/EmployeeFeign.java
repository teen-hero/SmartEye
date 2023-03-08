package com.yan.smarteye.purchase.feign;

import com.yan.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("smarteye-integration")
public interface EmployeeFeign {
    /**
     * 查询某职业类型的员工(不分页，直接返回全部)
     */
    @RequestMapping("/integration/employee/list/onetype")
    public R getOneTypeEE(@RequestParam("eeType")String eeType);
}
