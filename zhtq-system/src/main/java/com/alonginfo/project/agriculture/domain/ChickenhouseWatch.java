package com.alonginfo.project.agriculture.domain;

/**
 * 鸡舍检测点实体
 * @author 师登举
 * @date 2020年4月17日16:47:58
 */

public class ChickenhouseWatch {

    private Long chichenhouseWatchId;
    private Long chickenhouseId;
    private String date;
    private String time;
    private int weather;
    private String temperature;
    private String humidity;
    private String windDirection;
    private String windPower;
    private String oxygen;
    private String carbonDioxide;
    private String sun;
    private String chickenhouseTemperature;
    private String chickenhouseHumidity;

    public Long getChichenhouseWatchId() {
        return chichenhouseWatchId;
    }

    public void setChichenhouseWatchId(Long chichenhouseWatchId) {
        this.chichenhouseWatchId = chichenhouseWatchId;
    }

    public Long getChickenhouseId() {
        return chickenhouseId;
    }

    public void setChickenhouseId(Long chickenhouseId) {
        this.chickenhouseId = chickenhouseId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getWeather() {
        return weather;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindPower() {
        return windPower;
    }

    public void setWindPower(String windPower) {
        this.windPower = windPower;
    }

    public String getOxygen() {
        return oxygen;
    }

    public void setOxygen(String oxygen) {
        this.oxygen = oxygen;
    }

    public String getCarbonDioxide() {
        return carbonDioxide;
    }

    public void setCarbonDioxide(String carbonDioxide) {
        this.carbonDioxide = carbonDioxide;
    }

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public String getChickenhouseTemperature() {
        return chickenhouseTemperature;
    }

    public void setChickenhouseTemperature(String chickenhouseTemperature) {
        this.chickenhouseTemperature = chickenhouseTemperature;
    }

    public String getChickenhouseHumidity() {
        return chickenhouseHumidity;
    }

    public void setChickenhouseHumidity(String chickenhouseHumidity) {
        this.chickenhouseHumidity = chickenhouseHumidity;
    }
}
