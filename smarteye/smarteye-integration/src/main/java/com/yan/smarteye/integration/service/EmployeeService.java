package com.yan.smarteye.integration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yan.common.utils.PageUtils;
import com.yan.smarteye.integration.entity.EmployeeEntity;
import com.yan.smarteye.integration.vo.UserLoginVo;
import com.yan.smarteye.integration.vo.UserRegistVo;

import java.util.List;
import java.util.Map;

/**
 * 员工表
 */
public interface EmployeeService extends IService<EmployeeEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //条件查询
    PageUtils queryPageByCondition(Map<String, Object> params);
    //查询某职业类型的员工
    PageUtils queryPageByEEType(Map<String, Object> params);
    /**
     * 查询某职业类型的员工(不分页，直接返回全部)
     */
    List<EmployeeEntity> queryByEEType(String eeType);
    //保存注册的员工用户
    void register(UserRegistVo registerVo);
    //登录验证
    EmployeeEntity login(UserLoginVo loginVo);
}

