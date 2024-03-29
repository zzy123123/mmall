package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created By Zzyy
 **/
public class TokenCache {

    private static Logger logger= LoggerFactory.getLogger(TokenCache.class);//日志

    public static final String TOKEN_PREFIX="token_";
    //静态内存块
    //LRU算法
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000)
            .expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
                //默认的数值加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return null;//避免异常，null.equals会报空指针，所以换成String的null//或者后面用StringUtils.equals(,)
                }
            });

    public static void setKey(String key,String value){
        localCache.put(key, value);
    }

    public static String getKey(String key){
        String value =null;
        try{
            value=localCache.get(key);
            if(StringUtils.equals(null,value)){
                return null;
            }
            return value;
        }catch (Exception e){
            logger.error("localCache get error",e);
        }
        return null;
    }
}
