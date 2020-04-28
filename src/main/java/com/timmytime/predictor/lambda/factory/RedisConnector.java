package com.timmytime.predictor.lambda.factory;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisConnector {

    public static RedissonClient connect(String host) {

        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":6379");

        return Redisson.create(config);

    }


}
