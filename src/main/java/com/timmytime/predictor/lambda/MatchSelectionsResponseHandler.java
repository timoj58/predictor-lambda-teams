package com.timmytime.predictor.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmytime.predictor.lambda.factory.RedisConnector;
import com.timmytime.predictor.lambda.repo.MatchSelectionsResponse;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.UUID;

public class MatchSelectionsResponseHandler  implements RequestHandler<Map<String, String>, MatchSelectionsResponse> {

    private final RedissonClient redissonClient = RedisConnector.connect(System.getenv("AWS_REDIS_HOST"));

    @Override
    public MatchSelectionsResponse handleRequest(Map<String, String> stringStringMap, Context context) {

        String competition = stringStringMap.get("competition");
        UUID home = UUID.fromString(stringStringMap.get("home"));
        UUID away = UUID.fromString(stringStringMap.get("away"));

        RSet<String> repo = redissonClient.getSet("MatchSelectionsResponse_"+competition, new org.redisson.client.codec.StringCodec());

        //note there will always be an event if this is called.
      return repo.stream().map(m -> {
            try {
                return new ObjectMapper().readValue(m, MatchSelectionsResponse.class);
            } catch (JsonProcessingException e) {
                context.getLogger().log(e.getMessage());
                return null;
            }
        })
              .filter(f -> f.getHome().equals(home) && f.getAway().equals(away))
              .findFirst().get();
    }
}
