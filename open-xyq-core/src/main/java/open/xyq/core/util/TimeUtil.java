package open.xyq.core.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimeUtil {

    public static ZoneId CHINA_ZONE_ID = ZoneId.of("GMT+8");
    public static ZoneId UTC_ZONE_ID = ZoneId.of("GMT");

    public static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static String DEFAULT_TIME_PATTERN = "HH:mm:ss";
    public static String DEFAULT_DATETIME_PATTERN = DEFAULT_DATE_PATTERN + " " + DEFAULT_TIME_PATTERN;

    public static String TS_8_PATTERN = "yyyyMMdd";
    public static String TS_10_PATTERN = DEFAULT_DATE_PATTERN;
    public static String TS_14_PATTERN = "yyyyMMddHHmmss";
    public static String TS_17_PATTERN = "yyyyMMddHHmmssSSS";

    public static DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
    public static DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);
    public static DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN);
    public static DateTimeFormatter TS_8_PATTERN_FORMATTER = DateTimeFormatter.ofPattern(TS_8_PATTERN);
    public static DateTimeFormatter TS_10_PATTERN_FORMATTER = DateTimeFormatter.ofPattern(TS_10_PATTERN);
    public static DateTimeFormatter TS_14_PATTERN_FORMATTER = DateTimeFormatter.ofPattern(TS_14_PATTERN);
    public static DateTimeFormatter TS_17_PATTERN_FORMATTER = DateTimeFormatter.ofPattern(TS_17_PATTERN);

    private static final Map<String, DateTimeFormatter> formatterCache = new ConcurrentHashMap<String, DateTimeFormatter>() {{
        put(DEFAULT_DATE_PATTERN, DEFAULT_DATE_FORMATTER);
        put(DEFAULT_TIME_PATTERN, DEFAULT_TIME_FORMATTER);
        put(DEFAULT_DATETIME_PATTERN, DEFAULT_DATETIME_FORMATTER);
    }};

    public static String format(LocalDateTime localDateTime) {
        return DEFAULT_DATETIME_FORMATTER.format(localDateTime);
    }

    public static String format(LocalDateTime localDateTime, String pattern) {
        return formatterCache.computeIfAbsent(pattern, DateTimeFormatter::ofPattern).format(localDateTime);
    }

    public static String format(LocalDate localDate) {
        return DEFAULT_DATE_FORMATTER.format(localDate);
    }

    public static String format(LocalDate localDate, String pattern) {
        return formatterCache.computeIfAbsent(pattern, DateTimeFormatter::ofPattern).format(localDate);
    }

    // 2020-04-17
    public static String todayStr() {
        return LocalDate.now().format(DEFAULT_DATE_FORMATTER);
    }

    public static String utcTodayStr() {
        return LocalDateTime.now(UTC_ZONE_ID).format(DEFAULT_DATE_FORMATTER);
    }

    // 2020-04-16
    public static String yesterdayStr() {
        return LocalDate.now().minusDays(1).format(DEFAULT_DATE_FORMATTER);
    }

    public static String utcYesterdayStr() {
        return LocalDate.now(UTC_ZONE_ID).minusDays(1).format(DEFAULT_DATE_FORMATTER);
    }

    // 2020-04-18
    public static String tomorrowStr() {
        return LocalDate.now().plusDays(1).format(DEFAULT_DATE_FORMATTER);
    }

    public static String utcTomorrowStr() {
        return LocalDate.now(UTC_ZONE_ID).plusDays(1).format(DEFAULT_DATE_FORMATTER);
    }

    // 2020-04-17 14:39:14
    public static String nowStr() {
        return LocalDateTime.now().format(DEFAULT_DATETIME_FORMATTER);
    }

    // 2020-04-17 06:39:14
    public static String utcNowStr() {
        return LocalDateTime.now(UTC_ZONE_ID).format(DEFAULT_DATETIME_FORMATTER);
    }

    // 14:39:14
    public static String currentTimeStr() {
        return LocalTime.now().format(DEFAULT_TIME_FORMATTER);
    }

    public static String utcCurrentTimeStr() {
        return LocalTime.now(UTC_ZONE_ID).format(DEFAULT_TIME_FORMATTER);
    }

    // 20200417
    public static String currentTs8() {
        return LocalDateTime.now().format(TS_8_PATTERN_FORMATTER);
    }

    public static String utcCurrentTs8() {
        return LocalDateTime.now(UTC_ZONE_ID).format(TS_8_PATTERN_FORMATTER);
    }

    // 2020-04-17
    public static String currentTs10() {
        return LocalDateTime.now().format(TS_10_PATTERN_FORMATTER);
    }

    public static String utcCurrentTs10() {
        return LocalDateTime.now(UTC_ZONE_ID).format(TS_10_PATTERN_FORMATTER);
    }

    // 20200417063914
    public static String currentTs14() {
        return LocalDateTime.now().format(TS_14_PATTERN_FORMATTER);
    }

    public static String utcCurrentTs14() {
        return LocalDateTime.now(UTC_ZONE_ID).format(TS_14_PATTERN_FORMATTER);
    }

    // 20200417063914123
    public static String currentTs17() {
        return LocalDateTime.now().format(TS_17_PATTERN_FORMATTER);
    }

    public static String utcCurrentTs17() {
        return LocalDateTime.now(UTC_ZONE_ID).format(TS_17_PATTERN_FORMATTER);
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, formatterCache.computeIfAbsent(pattern, DateTimeFormatter::ofPattern));
    }

    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, formatterCache.computeIfAbsent(pattern, DateTimeFormatter::ofPattern));
    }

    public static long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    public static long toEpochSecond(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
    }

    public static LocalDateTime fromEpochMilli(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli).atZone(UTC_ZONE_ID).withZoneSameInstant(CHINA_ZONE_ID).toLocalDateTime();
    }

    public static LocalDateTime fromEpochMilli2UTC(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli).atZone(UTC_ZONE_ID).toLocalDateTime();
    }

    // parse ISO-8601 format zoned datetime
    public static ZonedDateTime parseZonedDateTime(String dateTimeStr) {
        return ZonedDateTime.parse(dateTimeStr);
    }

    public static LocalDateTime convertZone(LocalDateTime localDateTime, ZoneId from, ZoneId to) {
        return localDateTime.atZone(from).withZoneSameInstant(to).toLocalDateTime();
    }

    public static ZonedDateTime fromLegacy2ZonedDateTime(Date date) {
        return ZonedDateTime.from(date.toInstant().atZone(UTC_ZONE_ID));
    }

    public static LocalDateTime fromLegacy2UTC(Date date) {
        return fromLegacy2ZonedDateTime(date).withZoneSameInstant(UTC_ZONE_ID).toLocalDateTime();
    }

    public static LocalDateTime fromLegacy2China(Date date) {
        return fromLegacy2ZonedDateTime(date).withZoneSameInstant(CHINA_ZONE_ID).toLocalDateTime();
    }
}
