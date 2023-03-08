package com.yan.common.constant;

public class MaterialConstant {
    public enum MaterialTypeEnum{
        STOCK_TYPE_TYPE_BASE(1,"基本货物"), STOCK_TYPE_TYPE_OUT(0,"货架外货物");

        private int code;
        private String msg;

        MaterialTypeEnum(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }

    //物料申请单
    public enum MbillEunm{
        APPLYING(0,"申请中"),PROCESSING(1,"处理中"),
        FINISH(2,"已完成"),REFUSE(3,"拒绝申请"),REVOKE(4,"撤回申请");
        private int code;
        private String msg;

        MbillEunm(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }

    //物料申请单详情
    public enum MbilldetailEunm{
        APPLYING(0,"申请中"),RECEIVED(1,"已送达"),
        REFUSE(3,"拒绝申请"),REVOKE(4,"撤回申请");
        private int code;
        private String msg;

        MbilldetailEunm(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }
}
