package com.alonginfo.project.Weather.service;

import com.alonginfo.project.Weather.domain.WeatherResponse;

/**
 * 天气服务接口实现
 */
public interface WeatherDataService {

    /**
     * 根据城市id获取天气信息
     * @param cityId
     * @return
     */
    WeatherResponse getDataByCityId(String cityId);

    /**
     * 根据城市名称获取天气信息
     * @param cityName
     * @return
     */
    WeatherResponse getDataByCityName(String cityName);
}
