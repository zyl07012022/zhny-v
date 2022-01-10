package com.alonginfo.project.emergencyCommand.mapper;

import com.alonginfo.project.emergencyCommand.entity.EmergencyDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmergencyDictionaryMapper {

    /**
     * 查询字典信息
     */
    List<EmergencyDictionary> find(@Param("codeType")Integer codeType);
}
