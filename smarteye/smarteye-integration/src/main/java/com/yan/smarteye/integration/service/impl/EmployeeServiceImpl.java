package com.yan.smarteye.integration.service.impl;

import com.yan.smarteye.integration.exception.PhoneExistException;
import com.yan.smarteye.integration.exception.UserNameExistException;
import com.yan.smarteye.integration.vo.UserLoginVo;
import com.yan.smarteye.integration.vo.UserRegistVo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yan.common.utils.PageUtils;

import com.yan.smarteye.integration.dao.EmployeeDao;
import com.yan.smarteye.integration.entity.EmployeeEntity;
import com.yan.smarteye.integration.service.EmployeeService;


@Service("employeeService")
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, EmployeeEntity> implements EmployeeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EmployeeEntity> page = this.page(
                new Query<EmployeeEntity>().getPage(params),
                new QueryWrapper<EmployeeEntity>()
        );

        return new PageUtils(page);
    }
    //注册
    @Override
    public void register(UserRegistVo registerVo)  throws PhoneExistException, UserNameExistException {

        EmployeeEntity entity = new EmployeeEntity();
        // 设置默认叫料等级
        entity.setLevelNumb(0);

        // 检查手机号 用户名是否唯一 // 不一致则抛出异常
        if(this.baseMapper.selectCount(new QueryWrapper<EmployeeEntity>().eq("mobile", registerVo.getPhone())) > 0){
            throw new PhoneExistException();
        }
        if(this.baseMapper.selectCount(new QueryWrapper<EmployeeEntity>().eq("username", registerVo.getUserName())) > 0){
            throw new UserNameExistException();
        }

        // 密码要加密存储
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        entity.setPassword(bCryptPasswordEncoder.encode(registerVo.getPassword()));
        // 其他的信息(注意： 部门、职能信息还未填入)
        entity.setMobile(registerVo.getPhone());
        entity.setEeName(registerVo.getUserName());
        //TODO 保存前先审核 ....
        baseMapper.insert(entity);
    }

    //登录验证
    @Override
    public EmployeeEntity login(UserLoginVo loginVo) {
        String loginAccount = loginVo.getLoginacct();
        //以用户名或电话号登录的进行查询
        EmployeeEntity entity = this.getOne(new QueryWrapper<EmployeeEntity>().eq("ee_name", loginAccount).or().eq("mobile", loginAccount));
        if (entity!=null){
            //加密密码验证
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            boolean matches = bCryptPasswordEncoder.matches(loginVo.getPassword(), entity.getPassword());
            if (matches){
                //密码先置为空再返回前端
                entity.setPassword("");
                return entity;
            }
        }
        return null;
    }

    //条件查询
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<EmployeeEntity> queryWrapper = new QueryWrapper<>();
        //拼接模糊查询
        String key = (String) params.get("key");
        if (key!=null && !key.equals("")){
            queryWrapper.eq("ee_name",key).or().like("job_type",key);
        }
        //构造分页
        IPage<EmployeeEntity> page = this.page(
                new Query<EmployeeEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    //查询某职业类型的员工
    @Override
    public PageUtils queryPageByEEType(Map<String, Object> params) {
        QueryWrapper<EmployeeEntity> queryWrapper = new QueryWrapper<>();
        //拼接查询某职业类型的员工
        String type = (String)params.get("type");
        if (type!=null && !type.equals("")){
            queryWrapper.eq("job_type",type);
        }
        //拼接在职状态
        String status = (String) params.get("status");
        if (status!=null){
            queryWrapper.eq("status",status);
        }
        //构造分页
        IPage<EmployeeEntity> page = this.page(
                new Query<EmployeeEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 查询某职业类型的员工(不分页，直接返回全部)
     */
    @Override
    public List<EmployeeEntity> queryByEEType(String eeType) {
        QueryWrapper<EmployeeEntity> job_type = new QueryWrapper<EmployeeEntity>().eq("job_type", eeType);
        List<EmployeeEntity> employeeEntities = baseMapper.selectList(job_type);
        return employeeEntities;
    }
}