package com.yan.smarteye.stock.cache;

import java.util.HashMap;
import java.util.Map;

//单例对象 热启动
public class StockCacheHash {

    //缓存：stock 区间模糊查询 检索  热点key
    public static final Map<String,Integer> hash2 = new HashMap<>();
    static {
        hash2.put("金属材质",0);
        hash2.put("配件",0);
        hash2.put("高压材料",0);
        hash2.put("危险品",0);
    }
    //缓存：stock 区间模糊查询 检索  热点wlid
    public static final Map<String,Integer> hash3 = new HashMap<>();
    static {
        hash2.put("15",0);
        hash2.put("21",0);
        hash2.put("41",0);
    }

    public StockCacheHash(){

    }

}
