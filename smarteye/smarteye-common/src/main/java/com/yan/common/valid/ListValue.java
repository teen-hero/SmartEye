package com.yan.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
//自定义校验注解
@Documented
@Constraint(validatedBy = { ListValueConstraintValidator.class }) //指定校验器 这里指定我们自定义的ListValueConstraintValidator
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ListValue {
    //指定message信息的来源，可以指定文件位置，读取文件信息
//    String message() default "{com.yan.common.valid.ListValue.message}";
    //也可以直接在此写死
    String message() default "值必须是0或者1";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    //预先准备的值 vals={0,1}
    int[] vals() default { };
}


//对应实体类的注解
//@ListValue(vals={0,1},groups = {AddGroup.class, UpdateStatusGroup.class})
