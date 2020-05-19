package com.timmytime.predictor.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmytime.predictor.lambda.factory.RedisConnector;
import com.timmytime.predictor.lambda.repo.UpcomingCompetitionEventsResponse;
import com.timmytime.predictor.lambda.response.UpcomingEventResponse;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class EventsHandler implements RequestHandler<Map<String, String>, List<UpcomingCompetitionEventsResponse>> {

    private final RedissonClient redissonClient = RedisConnector.connect(System.getenv("AWS_REDIS_HOST"));

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public List<UpcomingCompetitionEventsResponse> handleRequest(Map<String, String> stringStringMap, Context context) {

        String country = stringStringMap.get("country");
        String competition = stringStringMap.get("competition");

        RSet<String> repo = redissonClient.getSet("UpcomingCompetitionEventsResponse", new org.redisson.client.codec.StringCodec());

        List<UpcomingCompetitionEventsResponse> data
                = repo.stream().map(m -> {
            try {
                return new ObjectMapper().readValue(m, UpcomingCompetitionEventsResponse.class);
            } catch (JsonProcessingException e) {
                return null;
            }
        })
                .collect(Collectors.toList());

        //need to apply filters...
        if (!country.trim().isEmpty() && !competition.trim().isEmpty()) {
            return data.stream().filter(f -> f.getCompetition().equals(competition)).collect(Collectors.toList());
        }

        Map<String, List<UpcomingEventResponse>> today = new HashMap<>();

        data.stream()
                .forEach(f ->
                        f.getUpcomingEventResponses().stream()
                                .forEach(event -> {
                                    if (dateFormat.format(event.getEventDate()).equals(dateFormat.format(new Date()))) {
                                        //this event is today....
                                        context.getLogger().log("we have a game today");

                                        if (today.containsKey(f.getCompetition())) {
                                            today.get(f.getCompetition()).add(event);
                                        } else {
                                            today.put(f.getCompetition(), new ArrayList<>());
                                            today.get(f.getCompetition()).add(event);
                                        }
                                    }
                                })
                );

        List<UpcomingCompetitionEventsResponse> eventsResponses = new ArrayList<>();

        today.keySet().stream().forEach(
                key -> eventsResponses.add(new UpcomingCompetitionEventsResponse(key, today.get(key)))
        );

        return eventsResponses;
    }
}
