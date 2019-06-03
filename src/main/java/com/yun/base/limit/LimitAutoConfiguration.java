package com.yun.base.limit;

import com.yun.base.limit.service.IYunLimitService;
import com.yun.base.limit.service.YunCounterLimitServiceImp;
import com.yun.base.limit.service.YunRedisLimitServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author: yun
 * @createdOn: 2019-05-30 13:05.
 */

@Configuration
@EnableConfigurationProperties(RedisLimitProperties.class)
@Import({RedisLimitAutoConfiguration.class, YunLimitAspect.class})
public class LimitAutoConfiguration {
    @Autowired
    private RedisLimitProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public IYunLimitService limitService() {
        IYunLimitService limit = null;
        if (properties.getType().equals("redis")) {
            limit = new YunRedisLimitServiceImp(properties);
        } else if (properties.getType().equals("counter")) {
            limit = new YunCounterLimitServiceImp(properties);
        }

        return limit;
    }
}
