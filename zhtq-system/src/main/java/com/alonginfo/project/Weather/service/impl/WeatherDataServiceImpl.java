package com.alonginfo.project.Weather.service.impl;

import com.alonginfo.common.utils.http.HttpUtils;
import com.alonginfo.project.Weather.domain.WeatherResponse;
import com.alonginfo.project.Weather.service.WeatherDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 天气实现
 */
@Service
public class WeatherDataServiceImpl implements WeatherDataService {

    // 天气信息接口地址
    private final String WEATHER_API = "https://www.tianqiapi.com/api/";


    /**
     * 根据城市编号获取信息
     * @param cityId
     * @return
     */
    @Override
    public WeatherResponse getDataByCityId(String cityId) {
        // 拼接请求参数的值
        String reStr = "version=v1&&appid=1001&appsecret=5566&cityid=" + cityId;
        String resW = HttpUtils.sendGet(WEATHER_API,reStr);
        ObjectMapper mapper = new ObjectMapper();
        WeatherResponse wr = null;
        try {
            wr = mapper.readValue(resW,WeatherResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wr;
    }

    /**
     * 根据城市名称获取信息
     * @param cityName
     * @return
     */
    @Override
    public WeatherResponse getDataByCityName(String cityName) {
        // 拼接请求参数的值
        String reStr = "version=v1&&appid=1001&appsecret=5566&city=" + cityName;
        String resW = HttpUtils.sendGet(WEATHER_API,reStr);
        ObjectMapper mapper = new ObjectMapper();
        WeatherResponse wr = null;
        try {
            wr = mapper.readValue(resW,WeatherResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wr;
    }
}
