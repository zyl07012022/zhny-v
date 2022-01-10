package com.alonginfo.project.alarmOrder.mapper;

import com.alonginfo.project.alarmOrder.entity.DeviceStatus;
import com.alonginfo.project.alarmOrder.entity.YcyjDevice;

import java.util.Map;

/**
 * @author 崔亚魁
 */
public interface AlarmOrderMapper {

    /**
     * 三项不平衡时间
     */
    Map<String,Object> selectUnbalanceTime(String transf_id);

    /**
     * 最大三项不平衡及数据时间
     */
    Map<String,Object> selectMaxUnbalance(String string);

    /**
     * 三项最大负载率
     */
    Map<String,Object> selectMaxLoad(String transf_id);

    /**
     * 过载/重载/正常时间
     */
    Map<String,Object> selectLoadTime(String transf_id);

    /**
     * 最低电压及时间
     */
    Map<String,Object> selectMinVolt(String transf_id);

    /**
     * 低电压时间
     */
    Map<String,Object> selectLowVoltCnt(String transf_id);

    /**
     * 高电压时间
     */
    Map<String,Object> selectHighVoltCnt(String transf_id);

    /**
     * 三项不平衡时间
     */
    YcyjDevice selectDeviceData(String transf_id);

    void saveErrorStatus(DeviceStatus deviceStatus);

    void insertYcStatus(Map map);
}
