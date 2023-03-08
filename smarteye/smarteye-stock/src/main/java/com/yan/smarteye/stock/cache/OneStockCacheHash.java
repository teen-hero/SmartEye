package com.yan.smarteye.stock.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//单例对象 热启动
public class OneStockCacheHash {
    //缓存：查询stockid下的所有详情    热点stockid
    public static final Map<Integer,Integer> hash1 = new HashMap<>();
    static {
        hash1.put(101,0);
        hash1.put(102,0);
        hash1.put(103,0);
    }
    //缓存：onestock 区间模糊查询 检索  热点key
    public static final Map<String,Integer> hash2 = new HashMap<>();
    static {
        hash2.put("齿轮",0);
        hash2.put("螺母",0);
        hash2.put("增压器",0);
    }
    //缓存：onestock 区间模糊查询 检索  热点wlid
    public static final Map<String,Integer> hash3 = new HashMap<>();
    static {
        hash2.put("15",0);
        hash2.put("21",0);
    }

    public OneStockCacheHash(){

    }

}
