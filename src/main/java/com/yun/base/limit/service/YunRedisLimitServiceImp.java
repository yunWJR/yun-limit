package com.yun.base.limit.service;

import com.yun.base.limit.RedisLimitPara;
import com.yun.base.limit.RedisLimitProperties;
import com.yun.base.limit.YunLimitException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: yun
 * @createdOn: 2019-06-03 09:27.
 */

public class YunRedisLimitServiceImp implements IYunLimitService {

    /**
     * 失败编码-触发了限流
     */
    private static final int FAIL_CODE = 0;

    private final RedisLimitProperties properties;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private Map<String, RedisLimitPara> paraMap;

    public YunRedisLimitServiceImp(RedisLimitProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        paraMap = new ConcurrentHashMap<String, RedisLimitPara>();
    }

    public void checkLimit(String key, int expireTime, int duration, int limitCount) {
        if (expireTime < 0) {
            expireTime = properties.getExpire_time();
        }

        if (duration < 0) {
            duration = properties.getDuration();
        }

        if (limitCount < 0) {
            limitCount = properties.getDuration();
        }

        RedisLimitPara para = getByKey(key, expireTime, duration, limitCount);

        Object result = limitRequest(para);

        if (FAIL_CODE == (Long) result) {
            throw new YunLimitException(para);
        }
    }

    private RedisLimitPara getByKey(String key, int expireTime, int duration, int limitCount) {
        RedisLimitPara para = paraMap.get(key);
        if (para == null) {
            para = new RedisLimitPara(key, properties.getBaseKey(), expireTime, duration, limitCount);

            paraMap.put(key, para);
        }

        return para;
    }

    private Object limitRequest(RedisLimitPara para) {
        Object result;

        long timeKey = System.currentTimeMillis() / 1000; // 按秒计算
        timeKey = timeKey / para.getDuration();

        // 时间作为 key
        String key = String.valueOf(timeKey);
        List<String> keys = Collections.singletonList(key);

        // 脚本
        DefaultRedisScript<Long> redisScript;
        redisScript = new DefaultRedisScript<Long>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptText(para.getScript());

        result = redisTemplate.execute(redisScript, keys, String.valueOf(para.getLimitCount()), String.valueOf(para.getExpire_time()));

        return result;
    }
}
