package com.alonginfo.project.Weather.domain;

import lombok.Data;

/**
 * 小时天气情况
 */
@Data
public class Hours {
    // 日期
    private String day;
    // 天气情况
    private String wea;
    // 温度
    private String tem;
    // 风向
    private String win;
    // 风速
    private String win_speed;
}
