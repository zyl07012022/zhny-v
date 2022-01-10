package com.alonginfo.utils;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import static com.alonginfo.project.gutian.constant.SceneConstant.*;
import static com.alonginfo.project.gutian.constant.SceneConstant.FORMAT_DATE;
import static com.alonginfo.project.gutian.constant.SceneConstant.GROUP_TYPE_MONTH;

/**
 * @author Li先生
 */
public class StrUtil {
    public static final String TYPE_WEEK = "WEEK";
    public static final String TYPE_MONTH = "MONTH";
    public static final String TYPE_YEAR = "YEAR";

    public static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * object 转 string
     * @param obj
     * @return
     */
    public static String Obj2Str(Object obj) {
        return obj == null ? "" :obj.toString();
    }



    /**
     * 查找需要查询的起始日期
     *
     * @param type 查询类型 WEEK,MONTH,YEAR
     * @return
     */
    public static String[] calcStartAndEndTime(String type) {
       if (TYPE_WEEK.equals(type)) {
            //本周
            LocalDate firstOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
            return new String[]{firstOfWeek.format(FORMAT_DATE),
                    firstOfWeek.plusDays(7).format(FORMAT_DATE)};
        } else if (TYPE_MONTH.equals(type)) {
            //本月
            return new String[]{LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).format(FORMAT_DATE),
                    LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth()).format(FORMAT_DATE)};
        } else if (TYPE_YEAR.equals(type)) {
            //本年
            return new String[]{LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).format(FORMAT_DATE),
                    LocalDate.now().with(TemporalAdjusters.firstDayOfNextYear()).format(FORMAT_DATE)};
        }

        return new String[]{null, null, null};
    }


    public static String BigDecimal2Str(BigDecimal big) {
        return big == null ? "":big.toString();
    }

    /**
     * 保留2位小数
     * @param big
     * @return
     */
    public static String BigDecimal2Str2(BigDecimal big) {
        return big == null ? "":big.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     *object 转 bigdecimal
     * @param active_power
     * @return
     */
    public static BigDecimal Obj2Bigdecimal(Object active_power) {
        return  active_power == null ? BigDecimal.ZERO:new BigDecimal(active_power.toString());
    }
}
