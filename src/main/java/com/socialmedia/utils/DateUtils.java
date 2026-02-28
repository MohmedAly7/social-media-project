package com.socialmedia.utils;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy");

    public static String formatForDisplay(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        Duration ago = Duration.between(instant, Instant.now());

        if (ago.toMinutes() < 1) {
            return "Just now";
        }
        if (ago.toMinutes() < 60) {
            long mins = ago.toMinutes();
            return mins + (mins == 1 ? " minute ago" : " minutes ago");
        }
        if (ago.toHours() < 24) {
            long hrs = ago.toHours();
            return hrs + (hrs == 1 ? " hour ago" : " hours ago");
        }
        if (ago.toDays() < 7) {
            long days = ago.toDays();
            return days + (days == 1 ? " day ago" : " days ago");
        }
        return dateTime.format(DATE_FORMAT);
    }
}
