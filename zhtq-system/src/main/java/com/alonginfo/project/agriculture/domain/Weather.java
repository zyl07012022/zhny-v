package com.alonginfo.project.agriculture.domain;

import com.alonginfo.framework.web.domain.BaseEntity;

/**
 * 气象实体类
 */
public class Weather  extends BaseEntity {
    private String date;
    private String time;
    private int weather;
    private float temperature;
    private float humidity;
    private int windDirection;
    private float windPower;
    private float oxygen;
    private float sun;

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

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public float getWindPower() {
        return windPower;
    }

    public void setWindPower(float windPower) {
        this.windPower = windPower;
    }

    public float getOxygen() {
        return oxygen;
    }

    public void setOxygen(float oxygen) {
        this.oxygen = oxygen;
    }

    public float getSun() {
        return sun;
    }

    public void setSun(float sun) {
        this.sun = sun;
    }
}
