package com.yun.base.limit.service;

/**
 * @author: yun
 * @createdOn: 2019-06-03 09:27.
 */

public interface IYunLimitService {
    public void checkLimit(String key, int expireTime, int duration, int limitCount);
}
