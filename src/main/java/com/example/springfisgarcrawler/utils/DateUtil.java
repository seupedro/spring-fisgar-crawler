package com.example.springfisgarcrawler.utils;

import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final String DASHED_DATETIME_PATTERN = "yyyy-MM-dd_HH-mm-ss";

    public static DateTimeFormatter toDashFormat() {
        return DateTimeFormatter.ofPattern(DASHED_DATETIME_PATTERN);
    }
}
