package com.alonginfo.project.emergencyCommand.mapper;

import com.alonginfo.project.emergencyCommand.entity.EmergencyVehicles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmergencyVehiclesMapper {
    /**
     * 查询抢修车辆表的全部数据
     */
    List<EmergencyVehicles> queryPage(@Param("affiliatedUnit")String affiliatedUnit,
                                      @Param("nameRepairCar")String nameRepairCar,
                                      @Param("numbeRepairCar")String numbeRepairCar,
                                      @Param("typeRepairCar")Integer typeRepairCar);
}
