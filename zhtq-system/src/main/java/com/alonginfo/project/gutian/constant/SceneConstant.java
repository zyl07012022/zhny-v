package com.alonginfo.project.gutian.constant;

import java.time.format.DateTimeFormatter;

/**
 * @Description 业务场景常量
 * @Author Yalon
 * @Date 2020-04-15 13:47
 * @Version mxdata_v
 */
public class SceneConstant {

    /**
     * 古田会议纪念馆
     */
    public static final String GUTIAN_MEMORIAL = "古田会议纪念馆";

    /**
     * 古田山庄
     */
    public static final String GUTIAN_VILLA = "古田山庄";

    /**
     * 古田干部学院
     */
    public static final String GUTIAN_COLLEGE = "古田干部学院";

    public static final String GROUP_TYPE_MONTH = "month";
    public static final String GROUP_TYPE_DAY = "day";
    public static final String GROUP_TYPE_HOUR = "hour";

    public static final String TYPE_HOUR = "HOUR";
    public static final String TYPE_DAY = "DAY";
    public static final String TYPE_WEEK = "WEEK";
    public static final String TYPE_MONTH = "MONTH";
    public static final String TYPE_YEAR = "YEAR";

    public static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final String PEAK_HOUR_START = "07";
    public static final String PEAK_HOUR_END = "19";
}
