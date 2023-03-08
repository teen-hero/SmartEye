package com.yan.smarteye.integration.controller;

import java.util.*;

import com.yan.common.exception.BizCodeEnume;
import com.yan.common.to.EmployeeEntityTo;
import com.yan.smarteye.integration.exception.PhoneExistException;
import com.yan.smarteye.integration.exception.UserNameExistException;
import com.yan.smarteye.integration.vo.UserLoginVo;
import com.yan.smarteye.integration.vo.UserRegistVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.smarteye.integration.entity.EmployeeEntity;
import com.yan.smarteye.integration.service.EmployeeService;
import com.yan.common.utils.PageUtils;
import com.yan.common.utils.R;



/**
 * 员工表
 */
@RestController
@RequestMapping("integration/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    //保存注册的员工用户
    @RequestMapping("/register")
    public R register(@RequestBody UserRegistVo registerVo) {
        try {
            employeeService.register(registerVo);
            //异常机制：通过捕获对应的自定义异常判断出现何种错误并封装错误信息
        } catch (UserNameExistException userException) {//用户已存在
            return R.error(BizCodeEnume.USER_EXIST_EXCEPTION.getCode(), BizCodeEnume.USER_EXIST_EXCEPTION.getMsg());
        } catch (PhoneExistException phoneException) {// 手机已经注册
            return R.error(BizCodeEnume.PHONE_EXIST_EXCEPTION.getCode(), BizCodeEnume.PHONE_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    //普通登录
    @RequestMapping("/login")
    public R login(@RequestBody UserLoginVo loginVo) {
        EmployeeEntity entity = employeeService.login(loginVo);
        if (entity!=null){  //其实可以改成返回ture or false
            return R.ok().put("data",entity);
        }else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION.getCode(), BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION.getMsg());
        }
    }

    /**
     * 修改状态
     */
    @RequestMapping("/update/status")
    public R updateStatus(@RequestBody EmployeeEntity employeeEntity){
        employeeService.updateById(employeeEntity);

        return R.ok();
    }

    /**
     * 列表,条件查询
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = employeeService.queryPageByCondition(params);


        return R.ok().put("page", page);
    }

    /**
     * 查询某职业类型的员工(分页查询)
     * 如：分配采购单，采购员查询
     */
    @RequestMapping("/list/eetype")
    public R listPurchase(@RequestParam Map<String, Object> params){
        PageUtils page = employeeService.queryPageByEEType(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询某职业类型的员工(不分页，直接返回全部)
     */
    @RequestMapping("/list/onetype")
    public R getOneTypeEE(@RequestParam("eeType") String eeType){
        List<EmployeeEntity> res = employeeService.queryByEEType(eeType);
        List<EmployeeEntityTo> tos = new ArrayList<>();
        for(EmployeeEntity entity : res){
            EmployeeEntityTo to = new EmployeeEntityTo();
            BeanUtils.copyProperties(entity,to);
            tos.add(to);
        }
        return R.ok().put("data", tos);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{eeId}")
    public R info(@PathVariable("eeId") Long eeId){
		EmployeeEntity employee = employeeService.getById(eeId);

        return R.ok().put("employee", employee);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody EmployeeEntity employee){
		employeeService.save(employee);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody EmployeeEntity employee){
		employeeService.updateById(employee);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] eeIds){
		employeeService.removeByIds(Arrays.asList(eeIds));

        return R.ok();
    }

}
