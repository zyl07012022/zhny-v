package com.alonginfo.project.gutian.constant;

/**
 * @Description 电气设备类型枚举类
 * @Author Yalon
 * @Date 2020-05-19 10:41
 * @Version mxdata_v
 */
public enum UnitType {

    NULL("null",""),
    DLJKT("1", "多联机空调"),
    FLRBZYKT("2", "风冷热泵中央空调"),
    DLJRBKT("3", "多联机热泵空调"),
    LSJZ("4", "冷水机组"),
    RSJZ("5", "热水机组"),
    KQNRSQ("6", "空气能热水器");

    private String code;

    private String name;

    private UnitType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static UnitType getEnum(String code) {
        for (UnitType emu : UnitType.values()) {
            if (emu.code.equals(code)) {
                return emu;
            }
        }
        return null;
    }

}
