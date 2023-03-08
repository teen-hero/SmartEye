package com.yan.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;
//自定义校验器，在里面定义校验规则
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {

    private Set<Integer> set = new HashSet<>();
    //初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {

        //从注解上获取值 遍历将其存入一个set
        int[] vals = constraintAnnotation.vals();
        for (int val : vals) {
            set.add(val);
        }

    }
    /**
     *判断是否校验成功
     * @param value 需要校验的值
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        //拿到传入的值 判断set中是否包含这个值
        //不包含返回false
        return set.contains(value);
    }
}
