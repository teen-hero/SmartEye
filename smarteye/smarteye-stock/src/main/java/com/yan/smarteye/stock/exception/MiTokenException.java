package com.yan.smarteye.stock.exception;

public class MiTokenException extends RuntimeException{
    public MiTokenException(){
        super("幂等令牌验证失败，不能保证幂等性");
    }

}
