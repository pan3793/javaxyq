package open.xyq.core.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class TimeUtilTest {

    @Test
    @SneakyThrows
    public void testLocal() {
        LocalDate startDate = TimeUtil.parseLocalDate("2017-11-01", "yyyy-MM-dd");
        assertEquals(LocalDate.of(2017, 11, 1), startDate);

        String dateTimeStr = "2020-04-28 06:45:21";
        String pattern = "yyyy-MM-dd HH:mm:ss";

        LocalDateTime logDateTime = TimeUtil.parseLocalDateTime(dateTimeStr, pattern);
        long epochMs = TimeUtil.toEpochMilli(logDateTime);
        long epochS = TimeUtil.toEpochSecond(logDateTime);
        assertEquals(1588027521000L, epochMs);
        assertEquals(1588027521L, epochS);
        assertEquals("2020-04-28",
                TimeUtil.format(logDateTime.toLocalDate(), TimeUtil.DEFAULT_DATE_PATTERN));
        assertEquals(2020, logDateTime.getYear());
        assertEquals(4, logDateTime.getMonthValue());
        assertEquals(28, logDateTime.getDayOfMonth());
        assertEquals(6, logDateTime.getHour());

        Date date = new SimpleDateFormat(pattern).parse(dateTimeStr);
        long ts = date.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertEquals(1588027521000L, ts);
        assertEquals("2020-04-28", dateTimeStr.substring(0, 10));
        assertEquals(2020, cal.get(Calendar.YEAR));
        assertEquals(4, cal.get(Calendar.MONTH) + 1);
        assertEquals(28, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(6, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals("1588027521", Long.toString(ts).substring(0, 10));
    }

    @Test
    public void testNow() {
        log.info("today: {}", TimeUtil.todayStr());
        log.info("UTC today: {}", TimeUtil.utcTodayStr());
        log.info("yesterday: {}", TimeUtil.yesterdayStr());
        log.info("UTC yesterday: {}", TimeUtil.utcYesterdayStr());
        log.info("tomorrow: {}", TimeUtil.tomorrowStr());
        log.info("UTC tomorrow: {}", TimeUtil.utcTomorrowStr());
        log.info("now: {}", TimeUtil.nowStr());
        log.info("UTC now: {}", TimeUtil.utcNowStr());

        log.info("current time: {}", TimeUtil.currentTimeStr());
        log.info("UTC current time: {}", TimeUtil.utcCurrentTimeStr());
        log.info("current ts8: {}", TimeUtil.currentTs8());
        log.info("UTC current ts8: {}", TimeUtil.utcCurrentTs8());
        log.info("current ts10: {}", TimeUtil.currentTs10());
        log.info("UTC current ts10: {}", TimeUtil.utcCurrentTs10());
        log.info("current ts14: {}", TimeUtil.currentTs14());
        log.info("UTC current ts14: {}", TimeUtil.utcCurrentTs14());
        log.info("current ts17: {}", TimeUtil.currentTs17());
        log.info("UTC current ts17: {}", TimeUtil.utcCurrentTs17());
    }

    @Test
    public void testZoned() {
        ZonedDateTime utcDateTime = TimeUtil.parseZonedDateTime("2020-01-14T03:32:18.139Z");
        assertEquals(TimeUtil.UTC_ZONE_ID.normalized(), utcDateTime.getZone().normalized());
        ZonedDateTime chinaDateTime = utcDateTime.withZoneSameInstant(TimeUtil.CHINA_ZONE_ID);
        assertEquals("2020-01-14 11:32:18", TimeUtil.format(chinaDateTime.toLocalDateTime(),
                TimeUtil.DEFAULT_DATETIME_PATTERN));

        String dateTimeStr = "2020-04-28 06:45:21";
        String pattern = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime dateTime = TimeUtil.parseLocalDateTime(dateTimeStr, pattern);

        LocalDateTime chinaTime =
                TimeUtil.convertZone(dateTime, TimeUtil.UTC_ZONE_ID, TimeUtil.CHINA_ZONE_ID);
        assertEquals("2020-04-28 14:45:21",
                TimeUtil.format(chinaTime, TimeUtil.DEFAULT_DATETIME_PATTERN));
    }

    @Test
    public void testFromEpochMilli() {
        assertEquals("1970-01-01 08:00:00",
                TimeUtil.fromEpochMilli(0).format(TimeUtil.DEFAULT_DATETIME_FORMATTER));

        assertEquals("2020-07-27 16:55:48", TimeUtil.format(TimeUtil.fromEpochMilli2UTC(Long.parseLong("1595868948070")), TimeUtil.DEFAULT_DATETIME_PATTERN));
    }

    @Test
    public void testLegacy() {
        ZonedDateTime utcEpochZonedDateTime = ZonedDateTime.of(LocalDateTime.of(1970, 1, 1, 0, 0), TimeUtil.UTC_ZONE_ID);
        ZonedDateTime chinaEpochZonedDateTime = ZonedDateTime.of(LocalDateTime.of(1970, 1, 1, 8, 0), TimeUtil.CHINA_ZONE_ID);
        Date date = new Date(0);
        ZonedDateTime zonedDateTime = TimeUtil.fromLegacy2ZonedDateTime(date);
        assertTrue(zonedDateTime.isEqual(utcEpochZonedDateTime));
        assertTrue(zonedDateTime.isEqual(chinaEpochZonedDateTime));
        assertEquals("1970-01-01 00:00:00",
                TimeUtil.fromLegacy2UTC(date).format(TimeUtil.DEFAULT_DATETIME_FORMATTER));
        assertEquals("1970-01-01 08:00:00",
                TimeUtil.fromLegacy2China(date).format(TimeUtil.DEFAULT_DATETIME_FORMATTER));
    }
}