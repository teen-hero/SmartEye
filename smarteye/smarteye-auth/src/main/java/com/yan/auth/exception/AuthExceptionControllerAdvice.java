package com.yan.auth.exception;

import com.yan.common.exception.BizCodeEnume;
import com.yan.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常统一处理，集中处理所有异常
 */
//@ResponseBody
//@ControllerAdvice(basePackages = "com.yan.auth.controller")
@RestControllerAdvice(basePackages = "com.yan.auth.controller") //指定处理哪个包下（位置）的异常
public class AuthExceptionControllerAdvice {

    @ExceptionHandler(value= NoUUIDException.class)    //uuid为空异常
    public void  handleNoUUIDException(NoUUIDException e){
        //默认不处理

    }
    //我们写代码测试的时候先注释掉它，否则把异常都捕获过来了，我们无法根据异常排错了，生产环境下再开启
//    @ExceptionHandler(value = Throwable.class)    //全部异常（上面的精确异常之外的异常）
//    public R handleException(Throwable throwable){
//        return R.error(BizCodeEnume.UNKNOW_EXEPTION.getCode(),BizCodeEnume.UNKNOW_EXEPTION.getMsg());
//    }
}
