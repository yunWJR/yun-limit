package com.yun.base.limit.service;

import com.yun.base.limit.RedisLimitProperties;
import com.yun.base.limit.YunLimitException;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: yun
 * @createdOn: 2019-06-03 09:55.
 */

public class YunCounterLimitServiceImp implements IYunLimitService {
    private final RedisLimitProperties properties;

    private Map<String, YunCounterLimit> limitMap;

    public YunCounterLimitServiceImp(RedisLimitProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        limitMap = new ConcurrentHashMap<String, YunCounterLimit>();
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

        YunCounterLimit limit = getByKey(key, expireTime, duration, limitCount);

        boolean passed = limit.grant();

        if (!passed) {
            throw new YunLimitException(key);
        }
    }

    private YunCounterLimit getByKey(String key, int expireTime, int duration, int limitCount) {
        YunCounterLimit limit = limitMap.get(key);
        if (limit == null) {
            limit = new YunCounterLimit(limitCount, duration);

            limitMap.put(key, limit);
        }

        return limit;
    }
}
