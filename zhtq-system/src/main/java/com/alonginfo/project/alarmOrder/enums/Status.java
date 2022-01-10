package com.alonginfo.project.alarmOrder.enums;

/**
 * @author 崔亚魁
 */

public enum Status {
    OVER_LOAD(1, "过载"), HEAVY_LOAD(2, "重载"),
    LOW_VOLTA(3, "A相低电压"),HIGH_VOLTA(4, "A相高电压"),
    LOW_VOLTB(5, "B相低电压"),HIGH_VOLTB(6, "B相高电压"),
    LOW_VOLTC(7, "C相低电压"),HIGH_VOLTC(8, "C相高电压"),
    UNBALANCE(9, "C相低电压"),
    UA_ERROR(10, "UA异常"), UB_ERROR(11, "UB异常"),UC_ERROR(12, "UC异常"),
    IA_ERROR(13, "IA异常"), IB_ERROR(14, "IA异常"),IC_ERROR(15, "IA异常"),
    PA_ERROR(16, "IA异常"), PB_ERROR(17, "IA异常"),PC_ERROR(18, "PC异常");

    private int code;
    private String info;

    Status(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
