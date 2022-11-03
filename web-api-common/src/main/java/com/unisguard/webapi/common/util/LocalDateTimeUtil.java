package com.unisguard.webapi.common.util;


import com.unisguard.webapi.common.dataobject.mon.MonHisDO;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalDateTimeUtil {

    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 指定时间 + 天数
     *
     * @param localDateTime
     * @param day
     * @return
     */
    public static LocalDateTime addDay(LocalDateTime localDateTime, int day) {
        return localDateTime.plusDays(day);
    }

    /**
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 差值
     */
    public static Long getTimeDiff(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            return 0L;
        }
        return Duration.between(startTime, endTime).toDays() + 1;
    }

    /**
     * 将时间字符串转换为时间戳,格式(YYY-MM-DD HH:mm:ss)
     */
    public static long getTimeFromDateStr(String s) {
        return LocalDateTime.parse(s, DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_TIME)).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 将时间字符串转换为时间戳,格式(YYY-MM-DD)
     */
    public static long getMill(String time) {
        return LocalDate.parse(time, DateTimeFormatter.ofPattern(DATE_FORMAT_DATE)).atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
    }

    /**
     * 如果数据在某个时间点有缺失需要补全
     *
     * @param showNum 显示数量
     * @return
     */
    public static List<MonHisDO> allMonHisList(int showNum, LocalDateTime endTime, List<MonHisDO> list) {
        List<MonHisDO> resultList = new ArrayList<>();
        Map<String,MonHisDO> map = new HashMap<>();
        int minute = Stream.of(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60).filter(i -> endTime.getMinute() < i).findFirst().orElse(5) - 5;
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.of(endTime.getHour(), minute);
        LocalDateTime localDateTime = LocalDateTime.of(localDate,localTime);
        for (int i = showNum -1; i >= 0; i--) {
            LocalDateTime time = localDateTime.minus(i * 5L, ChronoUnit.MINUTES).withSecond(0).withNano(0);
            MonHisDO monHisDOTemp = new MonHisDO();
            monHisDOTemp.setUpdateTime(time);
            map.put(time.toString(),monHisDOTemp);
        }
        list.forEach(item -> map.put(item.getUpdateTime().toString(),item));
        // 遍历map
        for (Map.Entry<String, MonHisDO> entry : map.entrySet()) {
            resultList.add(entry.getValue());
        }
        return resultList.stream().sorted(Comparator.comparing(MonHisDO::getUpdateTime)).collect(Collectors.toList());
    }
}
