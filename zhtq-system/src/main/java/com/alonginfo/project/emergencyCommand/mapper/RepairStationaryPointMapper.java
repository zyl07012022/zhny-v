package com.alonginfo.project.emergencyCommand.mapper;


import com.alonginfo.project.emergencyCommand.entity.RepairInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Description 农业场景 mapper
 * @Author Yalon
 * @Date 2020-04-14 16:52
 * @Version mxdata_v
 */
@Mapper
public interface RepairStationaryPointMapper {
    List<Map<String, String>> queryPage(RepairInfo repairInfo);
}
