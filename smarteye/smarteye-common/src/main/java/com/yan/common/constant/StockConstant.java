package com.yan.common.constant;
//用来库存服务中的保存常量
public class StockConstant {
    public enum StockTypeEnum{
        STOCK_TYPE_TYPE_BASE(1,"基本货物"), STOCK_TYPE_TYPE_OUT(0,"货架外货物");

        private int code;
        private String msg;

        StockTypeEnum(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }

    public enum OutbillStatusEnum{
        CREATE(0,"新建"), FINISH(1,"已完成");

        private int code;
        private String msg;

        OutbillStatusEnum(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }


    public enum OutbilldetailStatusEnum{
        CREATE(0,"新建"), FINISH(1,"已完成");

        private int code;
        private String msg;

        OutbilldetailStatusEnum(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }

    public enum OutbilldetailEnum{
        APPLYBILL(1,"物料申请单出库"),ARTIFICIAL(2,"手动出库"),OTHER(3,"其他出库");
        private int code;
        private String msg;

        OutbilldetailEnum(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }
}
