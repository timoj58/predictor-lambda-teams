package com.timmytime.predictor.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmytime.predictor.lambda.factory.RedisConnector;
import com.timmytime.predictor.lambda.response.TypeValidationResponse;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccuracyHandler implements RequestHandler<Map<String, String>, List<TypeValidationResponse>> {

    private final RedissonClient redissonClient = RedisConnector.connect(System.getenv("AWS_REDIS_HOST"));

    @Override
    public List<TypeValidationResponse> handleRequest(Map<String, String> stringStringMap, Context context) {

        String key = stringStringMap.get("key");
        RSet<String> typeValidations = redissonClient.getSet("TypeValidationResponse", new org.redisson.client.codec.StringCodec());


        return typeValidations
                .stream()
                .map(m -> {
                    try {
                        return new ObjectMapper().readValue(m, TypeValidationResponse.class);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                })
                .map(m -> key == null ? m :
                        new TypeValidationResponse(m.getType(), key, m.getValidations().get(key)))
                .filter(f -> !f.getValidations().isEmpty())
                .collect(Collectors.toList());

    }

}
