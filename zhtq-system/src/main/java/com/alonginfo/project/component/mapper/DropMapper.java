package com.alonginfo.project.component.mapper;

import com.alonginfo.project.component.entities.DropParams;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ljg
 * @Date 2021/7/15 13:43
 */
public interface DropMapper {
    List<DropParams> queryStList(@Param(value = "query") String query);

    List<DropParams> queryFeederList(DropParams dropParams);

    List<DropParams> queryDeviceList(Map map);

    LinkedHashMap<String, Object> queryOrgId();

    List<Map<String, Object>> queryParentId(String parent_id);

}
