package com.alonginfo.project.agriculture.provider;

import java.util.Map;

import static com.alonginfo.project.gutian.constant.SceneConstant.*;

/**
 * @Description 农业场景 复杂sql 提供器
 * @Author Yalon
 * @Date 2020-05-20 18:30
 * @Version mxdata_v
 */
public class AgricultureProvider {


    public String selectPowerUseGroupByType(Map<String, Object> params){
        String format = "";
        String groupType = (String) params.get("groupType");
        Boolean isPeak = (Boolean) params.get("isPeak");

        StringBuilder sql = new StringBuilder("");
        if (GROUP_TYPE_MONTH.equals(groupType)) {
            sql.append(" SELECT DATE_FORMAT(t1.groupDate, '%Y-%m') groupDate, SUM(t1.powerValue) powerValue FROM ( ");
            format = "%Y-%m-%d";
        } else if (GROUP_TYPE_DAY.equals(groupType)) {
            sql.append(" SELECT groupDate, SUM(t1.powerValue) powerValue FROM ( ");
            format = "%Y-%m-%d";
        } else if (GROUP_TYPE_HOUR.equals(groupType)) {
            sql.append(" SELECT groupDate, SUM(t1.powerValue) powerValue FROM ( ");
            format = "%H";
        }
        sql.append("    SELECT cue.ammeter_id ammeterId, DATE_FORMAT(cue.time, '").append(format).append("') groupDate, MAX(cue.ammeter_value)-MIN(cue.ammeter_value) powerValue ");
        sql.append("    FROM cps_use_electricity_agriculture cue LEFT JOIN cps_ammeter_agriculture caa ON cue.ammeter_id = caa.ammeter_id ");
        sql.append("     WHERE caa.base_type = #{baseType} ");
        sql.append("    AND cue.time >= STR_TO_DATE(#{startDate}, '%Y-%m-%d') AND cue.time < STR_TO_DATE(#{endDate}, '%Y-%m-%d') ");
        if (Boolean.TRUE.equals(isPeak)) {
            sql.append("    AND DATE_FORMAT(cue.time, '%H') >= '"+PEAK_HOUR_START+"' AND DATE_FORMAT(cue.time, '%H') < '"+PEAK_HOUR_END+"' ");
        }

        sql.append("    GROUP BY ammeterId,groupDate ");
        sql.append(" ) t1 GROUP BY groupDate ORDER BY groupDate ");
        return sql.toString();
    }
}
