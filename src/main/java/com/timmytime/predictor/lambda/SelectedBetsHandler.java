package com.timmytime.predictor.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmytime.predictor.lambda.factory.RedisConnector;
import com.timmytime.predictor.lambda.repo.SelectedBetResponse;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SelectedBetsHandler implements RequestHandler<Map<String, String>, List<SelectedBetResponse>> {

    private final RedissonClient redissonClient = RedisConnector.connect(System.getenv("AWS_REDIS_HOST"));


    @Override
    public List<SelectedBetResponse> handleRequest(Map<String, String> stringStringMap, Context context) {
        String market = stringStringMap.get("market");
        String event = stringStringMap.get("event");

        RSet<String> repo = redissonClient.getSet("SelectedBetResponse", new org.redisson.client.codec.StringCodec());

        return
                repo.stream()
                        .map(m -> {
                            try {
                                return new ObjectMapper().readValue(m, SelectedBetResponse.class);
                            } catch (JsonProcessingException e) {
                                return null;
                            }
                        })
                        .filter(f -> (market == null && event == null)
                                || (f.getMarket().equals(market) && f.getEvent().equals(event)))
                        .sorted(Comparator.comparing(SelectedBetResponse::getRating).reversed())
                        .collect(Collectors.toList());
    }
}
