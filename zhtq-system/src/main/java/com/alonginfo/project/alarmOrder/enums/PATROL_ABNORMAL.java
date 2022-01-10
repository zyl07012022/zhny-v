package com.alonginfo.project.alarmOrder.enums;

/**
 * @author 崔亚魁
 */
public enum PATROL_ABNORMAL {

    TG_LOWVOLT("39", "台区低压"),UNBLANCE("39", "过载"),OVERLOAD("39", "过载");

    private String code;
    private String info;

    PATROL_ABNORMAL(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
