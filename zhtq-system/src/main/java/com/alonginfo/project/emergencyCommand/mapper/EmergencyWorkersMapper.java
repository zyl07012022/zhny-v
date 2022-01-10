package com.alonginfo.project.emergencyCommand.mapper;

import com.alonginfo.project.agriculture.domain.Weather;
import com.alonginfo.project.agriculture.provider.AgricultureProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * @Description 农业场景 mapper
 * @Author Yalon
 * @Date 2020-04-14 16:52
 * @Version mxdata_v
 */
@Mapper
public interface EmergencyWorkersMapper {
    List<Map<String, String>> queryPage(@Param("team_name")String team_name,@Param("name") String name);

}
