package com.alonginfo.project.gutian.provider;


import com.alonginfo.common.utils.StringUtils;

import java.util.Map;

import static com.alonginfo.project.gutian.constant.SceneConstant.*;

/**
 * @Description 古田复杂sql提供
 * @Author Yalon
 * @Date 2020-04-16 11:33
 * @Version mxdata_v
 */
public class GutianProvider {

    private static final String HOLIDAY_CODE = "1";

    private static final String EVENT_CODE = "2";

    private static final String LOW_SEASON_CODE = "0";

    private static final String PEAK_SEASON_CODE = "1";

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
        sql.append("    FROM cps_use_electricity cue LEFT JOIN cps_building_group cbg ON cue.ammeter_id = cbg.ammeter_id ");
        sql.append("    LEFT JOIN cps_scene cs ON cbg.scene_id = cs.scene_id WHERE cs.scene_name = #{sceneName} ");
        sql.append("    AND cue.time >= STR_TO_DATE(#{startDate}, '%Y-%m-%d') AND cue.time < STR_TO_DATE(#{endDate}, '%Y-%m-%d') ");
        if (Boolean.TRUE.equals(isPeak)) {
                sql.append("    AND DATE_FORMAT(cue.time, '%H') >= '"+PEAK_HOUR_START+"' AND DATE_FORMAT(cue.time, '%H') < '"+PEAK_HOUR_END+"' ");
        }

        sql.append("    GROUP BY ammeterId,groupDate ");
        sql.append(" ) t1 GROUP BY groupDate ORDER BY groupDate ");
        return sql.toString();
    }

    public String selectPowerDistribute(Map<String, Object> params) {
        String quarterType = "quarter";
        String holidayType = "holiday";
        String seasonType = "season";
        String eventType = "event";
        String buildingId = (String) params.get("buildingId");
        String type = (String) params.get("type");
        StringBuilder subSql = new StringBuilder();
        subSql.append("   FROM    (SELECT cue.ammeter_id ammeterId, DATE_FORMAT(cue.time, '%Y-%m-%d') groupDate, MAX(cue.ammeter_value)-MIN(cue.ammeter_value) powerValue ")
            .append("         FROM  cps_use_electricity cue JOIN cps_building_group cbg ON cue.ammeter_id = cbg.ammeter_id ")
            .append("          JOIN cps_scene cs ON cbg.scene_id = cs.scene_id WHERE cs.scene_name = #{sceneName} ");
        if (StringUtils.isNotEmpty(buildingId)) {
            subSql.append("  AND cbg.building_id = #{buildingId} ");
        }
        subSql.append("        AND DATE_FORMAT(cue.time, '%Y') = #{year} GROUP BY ammeterId, DATE_FORMAT(cue.time,'%Y-%m-%d') ) t1 ")
            .append("    LEFT JOIN  ")
            .append("         (SELECT  DATE_FORMAT(cr.date, '%Y-%m-%d') receptionDate, cr.is_event isEvent, cr.is_peakseason isPeakSeason  ")
            .append("            FROM   cps_reception cr LEFT JOIN cps_scene cs ON cr.scene_id = cs.scene_id ")
            .append("           WHERE   cs.scene_name = #{sceneName} AND DATE_FORMAT(cr.date, '%Y') = #{year} ) t2 ")
            .append("      ON   t1.groupDate = t2.receptionDate  WHERE 1 = 1");

        StringBuilder sql = new StringBuilder(" SELECT");
        if (quarterType.equals(type)) {
            //按季度查
            sql.append(" CASE (CAST(DATE_FORMAT(t1.groupDate, '%m') AS SIGNED)+2) DIV 3 ")
                    .append("  WHEN 1 THEN '第一季度' WHEN 2 THEN '第二季度'")
                    .append("  WHEN 3 THEN '第三季度' WHEN 4 THEN '第四季度' END name, ")
                    .append(" ROUND(SUM(powerValue),2) value ").append(subSql)
                    .append("  GROUP BY name ");
        } else if (holidayType.equals(type)) {
            //按节假日查
            sql.append(" CASE t2.isEvent WHEN "+HOLIDAY_CODE+" THEN '节假日' ELSE '非节假日' END name,")
                    .append(" ROUND(SUM(powerValue),2)value  ").append(subSql)
                    .append(" AND t2.isEvent IS NOT NULL GROUP BY name ");
        } else if (seasonType.equals(type)) {
            //按淡旺季
            sql.append(" CASE t2.isPeakSeason WHEN "+LOW_SEASON_CODE+" THEN '淡季' WHEN "+PEAK_SEASON_CODE+" THEN '旺季' END name, ")
                    .append(" ROUND(SUM(powerValue),2) value  ").append(subSql)
                    .append(" AND t2.isPeakSeason IS NOT NULL GROUP BY name ");
        } else if (eventType.equals(type)) {
            //重大活动日
            sql.append(" CASE t2.isEvent WHEN "+EVENT_CODE+" THEN '重大活动日' ELSE '非重大活动日' END name,")
                    .append(" ROUND(SUM(powerValue),2) value  ").append(subSql)
                    .append(" AND t2.isEvent IS NOT NULL GROUP BY name ");
        }
        sql.append(" ORDER BY name ");
        return  sql.toString();
    }

    public String selectPowerUseGroupByBuildings(Map<String, Object> params) {
        Boolean isPeak = (Boolean) params.get("isPeak");
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT    t1.ammeterId ammeterId, MAX(t1.buildingName) buildingName, SUM(t1.powerValue) powerValue FROM ")
                .append("          (SELECT      cbg.ammeter_id ammeterId, MAX(cbg.building_name) buildingName, ")
                .append("                       DATE_FORMAT(cue.time, '%Y-%m-%d') groupDate , MAX(cue.ammeter_value) - MIN(cue.ammeter_value) powerValue ")
                .append("             FROM      cps_scene cs ")
                .append("             JOIN      cps_building_group cbg ")
                .append("               ON      cs.scene_id = cbg.scene_id ")
                .append("             JOIN      cps_use_electricity cue ")
                .append("               ON      cue.ammeter_id = cbg.ammeter_id ")
                .append("            WHERE      cs.scene_name = #{sceneName} ")
                .append("              AND      cue.time >= STR_TO_DATE(#{startDate}, '%Y-%m-%d') ")
                .append("              AND      cue.time < STR_TO_DATE(#{endDate}, '%Y-%m-%d') ");
        if (Boolean.TRUE.equals(isPeak)) {
            sql.append("                AND DATE_FORMAT(cue.time, '%H') >= '"+PEAK_HOUR_START+"' AND DATE_FORMAT(cue.time, '%H') < '"+PEAK_HOUR_END+"' ");
        }
        sql.append("                GROUP BY      ammeterId, groupDate ) t1 ")
                .append("   GROUP BY ammeterId ");

        return sql.toString();
    }

    public String selectPowerUseGroupByUnit(Map<String, Object> params) {
        Boolean isPeak = (Boolean) params.get("isPeak");
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT    t1.ammeterId ammeterId, MAX(t1.unitName) unitName, SUM(t1.powerValue) powerValue FROM ")
                .append("          (SELECT      ceu.ammeter_id ammeterId, MAX(ceu.elec_unit_name) unitName, ")
                .append("                       DATE_FORMAT(cue.time, '%Y-%m-%d') groupDate , MAX(cue.ammeter_value) - MIN(cue.ammeter_value) powerValue ")
                .append("             FROM      cps_scene cs ")
                .append("             JOIN      cps_building_group cbg ")
                .append("               ON      cs.scene_id = cbg.scene_id ")
                .append("             JOIN      cps_elec_unit ceu ")
                .append("               ON      cbg.building_id = ceu.building_group ")
                .append("             JOIN      cps_use_electricity cue ")
                .append("               ON      cue.ammeter_id = ceu.ammeter_id ")
                .append("            WHERE      cs.scene_name = #{sceneName} ")
                .append("              AND      cue.time >= STR_TO_DATE(#{startDate}, '%Y-%m-%d') ")
                .append("              AND      cue.time < STR_TO_DATE(#{endDate}, '%Y-%m-%d') ");
        if (Boolean.TRUE.equals(isPeak)) {
            sql.append("                AND DATE_FORMAT(cue.time, '%H') >= '"+PEAK_HOUR_START+"' AND DATE_FORMAT(cue.time, '%H') < '"+PEAK_HOUR_END+"' ");
        }
        sql.append("                GROUP BY      ammeterId, groupDate ) t1 ")
                .append("   GROUP BY ammeterId ");

        return sql.toString();
    }

    public String selectDnData(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time1")+"'),a.ammeter_value,0)),0) value1,\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time2")+"'),a.ammeter_value,0)),0) value2,\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time3")+"'),a.ammeter_value,0)),0) value3,\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time4")+"'),a.ammeter_value,0)),0) value4,\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time5")+"'),a.ammeter_value,0)),0) value5,\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time6")+"'),a.ammeter_value,0)),0) value6,\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time7")+"'),a.ammeter_value,0)),0) value7\n" +
                "FROM\n" +
                "	cps_use_electricity a\n" +
                "where a.ammeter_id in (");
        Object[] arr = (Object[]) params.get("idarr");
        for (int i = 0 ; i<arr.length;i++){
            if (i==arr.length-1){
                sql.append("'" + arr[i] + "')");
            }else {
                sql.append("'" + arr[i] + "',");
            }
        }
        return sql.toString();
    }
    public String selectNyDnData(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time1")+"'),a.ammeter_value,0)),0) value1,\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time2")+"'),a.ammeter_value,0)),0) value2,\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time3")+"'),a.ammeter_value,0)),0) value3,\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time4")+"'),a.ammeter_value,0)),0) value4,\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time5")+"'),a.ammeter_value,0)),0) value5,\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time6")+"'),a.ammeter_value,0)),0) value6,\n" +
                "	IFNULL(SUM(IF((DATE_FORMAT(a.time,'%Y-%m-%d %H:%i') = '"+params.get("time7")+"'),a.ammeter_value,0)),0) value7\n" +
                "FROM\n" +
                "	cps_use_electricity_agriculture a\n" +
                "where a.ammeter_id in (");
        Object[] arr = (Object[]) params.get("idarr");
        for (int i = 0 ; i<arr.length;i++){
            if (i==arr.length-1){
                sql.append("'" + arr[i] + "')");
            }else {
                sql.append("'" + arr[i] + "',");
            }
        }
        return sql.toString();
    }
}
