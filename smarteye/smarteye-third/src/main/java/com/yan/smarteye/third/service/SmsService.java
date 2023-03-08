package com.yan.smarteye.third.service;

public interface SmsService {
    //发送验证码短信
    public void send(String phone,String code) throws Exception;
    /**
     * 发送短信内容，预警库存
     */
    void sendWarnInfo(String phone, String msg);
}
