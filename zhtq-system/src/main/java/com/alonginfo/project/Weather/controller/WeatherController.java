package com.alonginfo.project.Weather.controller;

import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.Weather.domain.WeatherResponse;
import com.alonginfo.project.Weather.service.WeatherDataService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取天气实现控制层
 */
@Api(value="在线天气数据获取")
@RestController
@RequestMapping("/weather")
public class WeatherController extends BaseController {

    @Autowired
    private WeatherDataService weatherDataService;

    /**
     * 根据城市编号获取天气信息
     * @param cityId
     * @return
     */
    @GetMapping("/cityId/")
    public AjaxResult getReportByCityId(String cityId) {
        return AjaxResult.success(weatherDataService.getDataByCityId(cityId));
    }

    /**
     * 根据城市名称获取天气信息
     * @param cityName
     * @return
     */
    @GetMapping("/cityName/")
    public AjaxResult getReportByCityName(String cityName) {
        return AjaxResult.success(weatherDataService.getDataByCityName(cityName));
    }

}
