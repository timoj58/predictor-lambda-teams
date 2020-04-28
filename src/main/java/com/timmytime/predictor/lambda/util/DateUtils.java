package com.timmytime.predictor.lambda.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class DateUtils {


    /*
    at present, due to serialization etc and refactoring, the cache can hold normal java Dates.
     */
    public Function<LocalDateTime, Date> convert = date ->
            Date.from(date.atZone(ZoneId.systemDefault()).toInstant());

    public Function<LocalDateTime, Long> getMs = date ->
            date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

    public Function<Date, LocalDateTime> convertToLocalDateTime = date ->
            date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

    public Function<Date, LocalDate> convertToLocalDate = date ->
            date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    public Function<String, LocalDate> getLocalDate = fromDate ->
            LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    public Function<String, Date> getDate = fromDate ->
    {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(fromDate);
        } catch (ParseException e) {
            return Calendar.getInstance().getTime();
        }
    };

    public UnaryOperator<Date> today = now -> {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return sdf.parse(sdf.format(now));
        } catch (ParseException e) {
        }
        return Calendar.getInstance().getTime();
    };


}
