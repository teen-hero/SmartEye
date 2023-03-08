package com.yan.smarteye.ware.exception;

public class NoStockException extends RuntimeException{
    public NoStockException(){
        super("库存不足");
    }
}
