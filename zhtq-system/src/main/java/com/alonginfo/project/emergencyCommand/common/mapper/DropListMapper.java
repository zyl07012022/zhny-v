package com.alonginfo.project.emergencyCommand.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DropListMapper {

    @Select("SELECT guid,code_id,code_name,code_type FROM t_yj_qzqx_code WHERE code_type = #{codeType}")
    @ResultType(List.class)
    List<Map<String,Object>> selectDropList(@Param("codeType") String codeType);

    @Select("SELECT user_id,user_name FROM t_yj_qzqx_user")
    @ResultType(List.class)
    List<Map<String, Object>> selectUserName();

    @Select("SELECT courts_number,courts_name FROM t_yj_qzqx_courts")
    @ResultType(List.class)
    List<Map<String, Object>> selectCourtsName();
}
