package com.alonginfo.project.Weather.domain;

import lombok.Data;

import java.util.List;

/**
 * 在线天气实体类型
 */
@Data
public class WeatherOline {
    private String day;
    private String date;
    private String week;
    private String wea;
    private String wea_img;
    private String air;
    private String humidity;
    private String air_level;
    private String air_tips;
    private String tem1;
    private String tem2;
    private String tem;
    private String[] win;
    private String win_speed;
    private List<Hours> hours;
    private List<Index> index;
}
