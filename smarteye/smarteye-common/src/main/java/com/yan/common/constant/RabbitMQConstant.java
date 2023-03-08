package com.yan.common.constant;


public class RabbitMQConstant {

        //ware服务
        public static final String WARE_EXCHANGE="ware-exchange";
        public static final String WARE_DELAYQUUEUE="ware.delay.queue";
        public static final String WARE_DELAYTOUTINGKEY="ware.delay.key";
        public static final String WARE_RELEASEQUEUE="ware.release.queue";
        public static final String WARE_RELEASEROUTINGKEY="ware.release.key";
        public static final String WARE_ONESTOCKUPQUEUE="ware.onestockup.queue";
        public static final String WARE_ONESTOCKUPROUTINGKEY="ware.onestockup.key";
        public static final String WARE_BASEROUTINGKEY="ware.#";



        //stock服务
        public static final String STOCK_EXCHANGE="stock-exchange";
        public static final String STOCK_UPSTOCKQUEUE="stock.upstock.queue";
        public static final String STOCK_UPSTOCKROUTINGKEY="stock.upstock.key";

}
