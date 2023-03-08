package com.yan.common.exception;
/***
 * 业务状态码
 * 错误码列表：
 *  10: 通用
 *      001：参数格式校验
 *  11: auth认证
 *  12: 第三方组件
 *  13: 总库/货物
 *  14: 分库/物料
 *  15：采购
 *  16：综合服务
 *  17:库存
 */
public enum BizCodeEnume {
    NULLWARESTOCK_EXCEPTION(17101,"出库单中包含不存在的货物"),
    NOSTOCK_EXCEPTION(17100,"库存不足"),
    UNKNOW_EXEPTION(10000,"系统未知异常"),
    SMS_SEND_CODE_EXCEPTION(12100, "短信发送失败"),
    VALID_EXCEPTION( 10001,"参数格式校验失败"),
    USER_EXIST_EXCEPTION(16100, "用户已经存在"),
    PHONE_EXIST_EXCEPTION(16101, "手机号已经存在"),
    LOGINACCT_PASSWORD_EXCEPTION(16102, "用户名或密码错误"),
    SMS_CODE_EXCEPTION(11100, "短信获取频率太高,稍后再试"),
    LOGIN_FAIL(11101,"登陆失败"),
    LOGIN_CODE_EXCEPTION(11102,"验证码错误"),
    OUTBILL_FAIL(13101,"出库单异常，出库失败"),
    MITOKEN_FAIL(13102,"幂等验证失败"),
    SHELF_EMPTY(13103,"该货架为空"),
    ONESTOCKUP_FAIL(13104,"上架失败"),
    STOCK_UNEXIST(13100,"出库单中存在库存不存在的货物，请重试");



    private int code;
    private String msg;
    BizCodeEnume(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
