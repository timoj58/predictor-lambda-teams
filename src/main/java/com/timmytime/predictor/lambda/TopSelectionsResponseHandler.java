package com.timmytime.predictor.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmytime.predictor.lambda.factory.RedisConnector;
import com.timmytime.predictor.lambda.repo.TopSelectionsResponse;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TopSelectionsResponseHandler implements RequestHandler<Map<String, String>, List<TopSelectionsResponse>> {

    private final RedissonClient redissonClient = RedisConnector.connect(System.getenv("AWS_REDIS_HOST"));

    @Override
    public List<TopSelectionsResponse> handleRequest(Map<String, String> stringStringMap, Context context) {
        String competition = stringStringMap.get("competition");

        RSet<String> repo = redissonClient.getSet("TopSelectionsResponse_"+competition, new org.redisson.client.codec.StringCodec());

        return repo.stream().map(m -> {
            try {
                return new ObjectMapper().readValue(m, TopSelectionsResponse.class);
            } catch (JsonProcessingException e) {
                context.getLogger().log(e.getMessage());
                return null;
            }
        })
                .sorted(Comparator.comparing(TopSelectionsResponse::getOrder).reversed())
                .collect(Collectors.toList());

    }
}
