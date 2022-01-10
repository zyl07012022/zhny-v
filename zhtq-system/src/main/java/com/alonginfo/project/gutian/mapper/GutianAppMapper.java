package com.alonginfo.project.gutian.mapper;

import com.alonginfo.project.gutian.domain.ElectricRealTimeInfo;
import com.alonginfo.project.gutian.provider.GutianProvider;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description 古田三大业务场景-app Mapper
 * @Author Yalon
 * @Date 2020-04-15 13:40
 * @Version mxdata_v
 */
@Mapper
public interface GutianAppMapper {


    /**
     * 当前园区用电功率
     * @return
     */
    BigDecimal getDqyqgl(String sceneId);

    /**
     * 当前园区累积能耗
     * @return
     */
    BigDecimal getDqyqljnh(String sceneId);

    /**
     * 指定月访问人数-
     * @return
     */
    BigDecimal getCountreception(@Param("date") String date,@Param("sceneId") String sceneId);

    /**
     * 面积
     * @return
     */
    BigDecimal getSceneAcreage(@Param("sceneId") String sceneId);

    /**
     * 指定月用电量
     * @return
     */
    BigDecimal getSumAmmeterValue(@Param("date") String date,@Param("sceneId") String sceneId);

    /**
     * 今日用电量-
     * @param sceneId
     * @return
     */
    List<Map<String,Object>> getJrydl (@Param("sceneId") String sceneId);

    /**
     * 当前用电功率
     * @param sceneId
     * @return
     */
    List<Map<String,Object>> getDqydgl (@Param("sceneId") String sceneId);

    /**
     * 所有楼宇
     * @param sceneId
     * @return
     */
    List<Map<String,Object>> getSyly (@Param("sceneId") String sceneId);


    /**
     * 设备今日用电量
     * @param sceneId
     * @return
     */
    List<Map<String,Object>> getSbjrydl (@Param("sceneId") String sceneId);

    /**
     * 设备当前用电功率
     * @param sceneId
     * @return
     */
    List<Map<String,Object>> getSbdqydgl (@Param("sceneId") String sceneId);

    /**
     * 所有设备
     * @param sceneId
     * @return
     */
    List<Map<String,Object>> getSysb (@Param("sceneId") String sceneId);


    /**
     * 楼宇用能检测
     * @param buildingId
     * @param startTime
     * @param endTime
     * @return
     */
   BigDecimal getLyYnjc(@Param("buildingId") String buildingId,@Param("startTime")String startTime,@Param("endTime")String endTime);

    /**
     *  峰值电量
     * @param buildingId
     * @param s
     * @param s1
     * @return
     */
    BigDecimal getLyYnjcFzdl(@Param("buildingId") String buildingId,@Param("startTime")String startTime,@Param("endTime")String endTime);

    /**
     * 查询最大时间
     * @return
     */
    String getMaxTime(String buildingId);

    /**
     * 查询当前功率
     * @param buildingId
     * @param date
     * @return
     */
    List<Map<String,Object>> getDqgl(@Param("buildingId") String buildingId,@Param("date") String date);

    /**
     * 查询上一时段功率
     * @param buildingId
     * @param date
     * @return
     */
    List<Map<String,Object>> getSydgl(@Param("buildingId")String buildingId,@Param("date")String date);

    /**
     * 设备用能检测
     * @param elecUnitId
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal getSbYnjc(@Param("elecUnitId") String elecUnitId,@Param("startTime")String startTime,@Param("endTime")String endTime);

    /**
     *  设备峰值电量
     * @param elecUnitId
     * @param startTime
     * @param  endTime
     * @return
     */
    BigDecimal getSbYnjcFzdl(@Param("elecUnitId") String elecUnitId,@Param("startTime")String startTime,@Param("endTime")String endTime);


    /**
     * 查询设备最大时间
     * @return
     */
    String getSbMaxTime(String buildingId);

    /**
     * 查询设备当前功率
     * @param buildingId
     * @param date
     * @return
     */
    Map<String,Object> getSbDqgl(@Param("buildingGroup") String buildingId,@Param("date") String date);

    /**
     * 查询设备上一时段功率
     * @param buildingId
     * @param date
     * @return
     */
    Map<String,Object> getSbSydgl(@Param("buildingGroup")String buildingId,@Param("date")String date);

    /**
     * 用能-设备能耗
     * @param sceneId
     */
    List<Map<String,Object>> getSbnh(String sceneId);

    /**
     * 获取在线设备  15分钟有数据上报
     * @return
     */
    List<Map<String,Object>> getSbOnline(String sceneId);


    @Select("SELECT COUNT(a.scene_id) from cps_reception a where a.scene_id = #{sceneId} and DATE_FORMAT(a.date,'%Y-%m-%d') = #{param.time} ")
    int selectReceptionnum(@Param("param") Map<String, Object> param,@Param("sceneId") String scene_id);
    @Insert("INSERT INTO  `cps_reception` (`scene_id`,  `date`, `reception` , `is_event`,`is_peakseason`) values (#{sceneId}, str_to_date(#{param.time},'%Y-%m-%d'),#{param.reception},#{param.is_event},#{param.is_peakseason})")
    void insertReception(@Param("param")Map<String, Object> param,@Param("sceneId") String sceneId);
    @Update("update  `cps_reception` set reception = #{param.reception}, is_event=#{param.is_event},is_peakseason=#{param.is_peakseason} where scene_id = #{sceneId} and DATE_FORMAT(date,'%Y-%m-%d')  = #{param.time} ")
    void updateReception(@Param("param")Map<String, Object> param,@Param("sceneId") String sceneId);


}
