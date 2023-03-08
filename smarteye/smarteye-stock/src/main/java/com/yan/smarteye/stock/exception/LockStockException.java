package com.yan.smarteye.stock.exception;

public class LockStockException extends RuntimeException{
    public LockStockException(){
        super("锁定库存失败");
    }
}
