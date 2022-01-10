package com.alonginfo.project.emergencyCommand.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface RescueTeamsMapper {
    List<Map<String, String>> queryPage(@Param("affiliated_unit") String affiliated_unit, @Param("team_name") String team_name, @Param("resource_categories") String resource_categories, @Param("resource_type") String resource_type);

}
