package com.alonginfo.project.fileParse.mapper;

import com.alonginfo.project.alarmOrder.entity.DeviceFaultInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ljg
 * @date 2021/2/8
 */
@Mapper
public interface FADataMapper {
    @Insert("INSERT INTO `emergency_command`.`ts_file_info`(`ID`,`M_RID`, `DEV_NAME`, `PSR_TYPE`,`MESSAGE_TIME`,`TRIP_TIME`, `ACCIDENT_RANGE`, `CONTENT`, `FAULT_TYPE`, `UPDATE_TIME`, `DEVICE_ID`) VALUES (NULL,#{mRID}, #{DevName},#{PSRType}, #{MessageTime}, #{TripTime}, #{Accident_range}, #{Content}, #{fault_type}, NOW(), #{DEVICE_ID});\n")
    Integer addFileInfo(Map<String, String> map);

    @Insert("INSERT INTO `emergency_command`.`ts_device_info`(`ID`, `DEVICE_ID`, `M_RID`, `DEV_NAME`, `PARENT_NAME`, `UPDATE_TIME`) VALUES (NULL,#{DEVICE_ID}, #{mRID}, #{DevName}, #{parentName}, NOW());\n")
    Integer addDeviceInfo(Map<String, String> map2);

    List<Map<String, Object>> getDMSData(@Param("table") String table,@Param("deviceId") String deviceId);

    String getCodeName(@Param("code_name") String code_name);

    Integer updateDms(@Param("emergency_number") String emergency_number,@Param("FAULT_TYPE") String FAULT_TYPE);

    Integer addFaultInfo(Map<String,List<DeviceFaultInfo>> map);
}
