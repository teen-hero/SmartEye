package com.yan.smarteye.ware.exception;

public class NullWareStock extends RuntimeException{
    public NullWareStock(){
        super("出库单中包含不存在的货物");
    }
}
