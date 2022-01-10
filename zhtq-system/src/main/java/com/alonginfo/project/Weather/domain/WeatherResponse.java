package com.alonginfo.project.Weather.domain;

import lombok.Data;

/**
 * 天气数据返回实体
 */
@Data
public class WeatherResponse {
    /**
    * 消息数据
    */
    private WeatherOline data;
    private String cityid;
    private String update_time;
    private String city;
    private String cityEn;
    private String country;
    private String countryEn;
}
