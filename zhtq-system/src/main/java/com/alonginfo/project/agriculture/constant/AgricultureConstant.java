package com.alonginfo.project.agriculture.constant;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * @Description 农业场景常量
 * @Author Yalon
 * @Date 2020-04-14 17:41
 * @Version mxdata_v
 */
public class AgricultureConstant {

    /**
     * 鸡舍
     */
    public static final String HOUSE_TYPE_CHICKEN = "chickenHouse";

    /**
     * 鸡舍表名
     */
    public static final String TABLE_NAME_CHICKEN = "cps_chickenhouse";

    /**
     * 鸡舍主键
     */
    public static final String HOUSE_KEY_CHICKEN = "chickenhouse_id";

    /**
     * 鸡舍名字列
     */
    public static final String HUSE_NAME_CHICKEN = "chickenhouse_name";


    /**
     * 大棚
     */
    public static final String HOUSE_TYPE_GREEN = "greenHouse";

    /**
     * 大鹏表名
     */
    public static final String TABLE_NAME_GREEN = "cps_greenhouse";

    /**
     * 大棚主键
     */
    public static final String HOUSE_KEY_GREEN = "greenhouse_id";

    /**
     * 鸡舍名字列
     */
    public static final String HUSE_NAME_GREEN = "greenhouse_name";

    public static final String TYPE_DAY = "DAY";
    public static final String TYPE_WEEK = "WEEK";
    public static final String TYPE_MONTH = "MONTH";
    public static final String TYPE_YEAR = "YEAR";

    public static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final String FORMAT_DATE_DAY = "dd";

    /**
     * 蛋与鸡转化比例 暂定 1:80
     */
    public static final BigDecimal converIndex = new BigDecimal("0.0125");

}
