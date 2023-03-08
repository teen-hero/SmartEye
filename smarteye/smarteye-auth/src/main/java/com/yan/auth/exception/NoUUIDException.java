package com.yan.auth.exception;

public class NoUUIDException extends RuntimeException{
    public NoUUIDException(){
        super("验证码所需uuid不能为空");
    }
}
