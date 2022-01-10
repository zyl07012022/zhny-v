package com.alonginfo.project.gutian.service.impl;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.project.gutian.mapper.GutianAppMapper;
import com.alonginfo.project.gutian.service.IGutianAppService;
import com.alonginfo.utils.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 三大业务场景-app  业务层实现
 * @Author Yalon
 * @Date 2020-04-15 15:48
 * @Version mxdata_v
 */
@Slf4j
@Service
public class GutianAppServiceImpl implements IGutianAppService {




    @Autowired
    private GutianAppMapper gutianAppMapper;


    /**
     * 古田干部学院-当前园区用电功率
     * @return
     */
    @Override
    public String getDqyqgl(String sceneId) {
       BigDecimal res =  gutianAppMapper.getDqyqgl(sceneId);
       // 如果是空值 返回0
       if (res == null) {
           return "0";
       } else {
           // 保留2位小数
           return res.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
       }
    }

    /**
     * 古田干部学院-当前园区累积能耗
     * @return
     */
    @Override
    public String getDqyqljnh(String sceneId) {
        BigDecimal res =  gutianAppMapper.getDqyqljnh(sceneId);
        // 如果是空值 返回0
        if (res == null) {
            return "0";
        } else {
            // 保留2位小数
            return res.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    /**
     *  古田干部学院-能耗指标
     * @param date
     * @return
     */
    @Override
    public Map<String, String> getNhzb(String date,String sceneId) {
        Map<String,String> res = new HashMap<>();
        // 默认没有值
        res.put("rjhd","-");
        res.put("mpmhd","-");
        // 访问人数
        BigDecimal countreception =  gutianAppMapper.getCountreception(date,sceneId);
        // 面积
        BigDecimal sceneAcreage =  gutianAppMapper.getSceneAcreage(sceneId);
        // 总用电量
        BigDecimal sumAmmeterValue =  gutianAppMapper.getSumAmmeterValue(date,sceneId);
        if (sumAmmeterValue == null || sumAmmeterValue.intValue() == 0) {
            return res;
        }
        // 人均耗电
        if (countreception != null && sumAmmeterValue.intValue() != 0) {
            BigDecimal rjhd = sumAmmeterValue.divide(countreception,4,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP);
            res.put("rjhd",rjhd.toString());
        }
        // 每平米耗电
        if (sceneAcreage != null && sceneAcreage.intValue() != 0) {
            BigDecimal mpmhd = sumAmmeterValue.divide(sceneAcreage,4,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP);
            res.put("mpmhd",mpmhd.toString());
        }
        return res;
    }


    /**
     *  楼宇群用能监测
     * @return
     */
    @Override
    public List<Map<String, String>> getLyqynjc(String sceneId) {
        List<Map<String,String>> res = new ArrayList<>();
        final List<Map<String, Object>> jrydl = gutianAppMapper.getJrydl(sceneId);
        List<Map<String, Object>> dqydgl = gutianAppMapper.getDqydgl(sceneId);
        List<Map<String, Object>> syly = gutianAppMapper.getSyly(sceneId);

        if (syly == null || syly.size()==0 ) {
            return res;
        }

        syly.stream().forEach(map->{
            // 楼宇id
            String building_id = StrUtil.Obj2Str(map.get("building_id"));
            // 楼宇名字
            String building_name = StrUtil.Obj2Str(map.get("building_name"));
            // 电表id
            String ammeter_id = StrUtil.Obj2Str(map.get("ammeter_id"));

            Map<String,String> dataMap = new HashMap<>();
            dataMap.put("buildingId",building_id);
            dataMap.put("buildingName",building_name);
            dataMap.put("ammeterId",ammeter_id);
            // 默认值
            dataMap.put("jrydl","-");
            dataMap.put("dqydgl","-");
            // 今日用电量
            if (jrydl != null || jrydl.size()>0 ) {
                jrydl.stream().forEach(jmap->{
                    // 电表id
                    String jammeter_id = StrUtil.Obj2Str(jmap.get("ammeter_id"));
                    // 如果是同一个电表
                    if (StringUtils.equals(ammeter_id,jammeter_id)) {
                        String val = StrUtil.Obj2Str(jmap.get("val"));
                        dataMap.put("jrydl",val);
                    }
                });
            }
            // 当前用电功率
            if (dqydgl != null ||  dqydgl.size()>0 ) {
                dqydgl.stream().forEach(dmap->{
                    // 电表id
                    String dammeter_id = StrUtil.Obj2Str(dmap.get("ammeter_id"));
                    // 如果是同一个电表
                    if (StringUtils.equals(ammeter_id,dammeter_id)) {
                        String active_power = StrUtil.Obj2Str(dmap.get("active_power"));
                        dataMap.put("dqydgl",active_power);
                    }
                });
            }
            // 加入结果集中
            res.add(dataMap);
        });
        return res;
    }

    /**
     * 楼层级电气设备用能监测
     * @param sceneId
     * @return
     */
    @Override
    public List<Map<String, String>> getLcjdqsbynjc(String sceneId) {
        List<Map<String,String>> res = new ArrayList<>();
        // 设备今日用电量
        final List<Map<String, Object>> sbjrydl = gutianAppMapper.getSbdqydgl(sceneId);
        // 设备当前用电功率
        List<Map<String, Object>> sbdqydgl = gutianAppMapper.getSbdqydgl(sceneId);
        // 所有设备
        List<Map<String, Object>> sysb = gutianAppMapper.getSysb(sceneId);

        if (sysb == null || sysb.size()==0 ) {
            return res;
        }

        sysb.stream().forEach(map->{
            // 设备id
            String elec_unit_id = StrUtil.Obj2Str(map.get("elec_unit_id"));
            // 设备名称
            String elec_unit_name = StrUtil.Obj2Str(map.get("elec_unit_name"));
            // 电表id
            String ammeter_id = StrUtil.Obj2Str(map.get("ammeter_id"));
            // 所属楼宇
            String building_group = StrUtil.Obj2Str(map.get("building_group"));

            Map<String,String> dataMap = new HashMap<>();
            dataMap.put("elecUnitId",elec_unit_id);
            dataMap.put("elecUnitName",elec_unit_name);
            dataMap.put("ammeterId",ammeter_id);
            dataMap.put("buildingGroup",building_group);
            dataMap.put("jrydl","-");
            dataMap.put("dqydgl","-");
            // 今日用电量
            if (sbjrydl != null || sbjrydl.size()>0 ) {
                sbjrydl.stream().forEach(jmap->{
                    // 电表id
                    String jammeter_id = StrUtil.Obj2Str(jmap.get("ammeter_id"));
                    // 如果是同一个电表
                    if (StringUtils.equals(ammeter_id,jammeter_id)) {
                        String val = StrUtil.Obj2Str(jmap.get("active_power"));
                        dataMap.put("jrydl",val);
                    }
                });
            }
            // 当前用电功率
            if (sbdqydgl != null ||  sbdqydgl.size()>0 ) {
                sbdqydgl.stream().forEach(dmap->{
                    // 电表id
                    String dammeter_id = StrUtil.Obj2Str(dmap.get("ammeter_id"));
                    // 如果是同一个电表
                    if (StringUtils.equals(ammeter_id,dammeter_id)) {
                        String active_power = StrUtil.Obj2Str(dmap.get("active_power"));
                        dataMap.put("dqydgl",active_power);
                    }
                });
            }

            res.add(dataMap);
        });
        return res;
    }

    /**
     * 楼宇区域监测
     * @param buildingId
     * @return
     */
    @Override
    public Map<String, Object> getLyqyjc(String buildingId) {
        Map<String,Object> res = new HashMap<>();

        // 用能检测
        List<Map<String,String>> ynjc = new ArrayList<>();
         //本周
        Map<String,String> weekmap = new HashMap<>();
        String[] week =StrUtil.calcStartAndEndTime("WEEK");
        // 总电量
        BigDecimal weeksum = gutianAppMapper.getLyYnjc(buildingId,week[0],week[1]);
        // 峰值电量
        BigDecimal weekfzdl = gutianAppMapper.getLyYnjcFzdl(buildingId,week[0],week[1]);
        weekmap.put("zqx","本周");
        weekmap.put("fzdl",StrUtil.BigDecimal2Str2(weekfzdl));
        weekmap.put("zdl",StrUtil.BigDecimal2Str2(weekfzdl));
        weekmap.put("gzdl","");
        if (weeksum != null && weekfzdl != null) {
            BigDecimal  gzdl =weeksum.subtract(weekfzdl);
            weekmap.put("gzdl",StrUtil.BigDecimal2Str2(gzdl));
        }


        //本月
        String[] month =StrUtil.calcStartAndEndTime("WEEK");
        Map<String,String> monthmap = new HashMap<>();
        // 总电量
        BigDecimal monthsum = gutianAppMapper.getLyYnjc(buildingId,month[0],month[1]);
        // 峰值电量
        BigDecimal monthfzdl = gutianAppMapper.getLyYnjcFzdl(buildingId,month[0],month[1]);
        monthmap.put("zqx","本月");
        monthmap.put("fzdl",StrUtil.BigDecimal2Str2(monthfzdl));
        monthmap.put("zdl",StrUtil.BigDecimal2Str2(monthsum));
        monthmap.put("gzdl","");
        if (monthsum != null && monthfzdl != null) {
            BigDecimal  gzdl =monthsum.subtract(monthfzdl);
            monthmap.put("gzdl",StrUtil.BigDecimal2Str2(gzdl));
        }


        //本年
        Map<String,String> yearmap = new HashMap<>();
        String[] year =StrUtil.calcStartAndEndTime("YEAR");
        // 总电量
        BigDecimal yearsum = gutianAppMapper.getLyYnjc(buildingId,year[0],year[1]);
        // 峰值电量
        BigDecimal yearfzdl = gutianAppMapper.getLyYnjcFzdl(buildingId,year[0],year[1]);
        yearmap.put("zqx","本年");
        yearmap.put("fzdl",StrUtil.BigDecimal2Str2(yearfzdl));
        yearmap.put("zdl",StrUtil.BigDecimal2Str2(yearsum));
        yearmap.put("gzdl","");
        if (yearsum != null && yearfzdl != null) {
            BigDecimal  gzdl =yearsum.subtract(yearfzdl);
            yearmap.put("gzdl",StrUtil.BigDecimal2Str2(gzdl));
        }

        ynjc.add(weekmap);
        ynjc.add(monthmap);
        ynjc.add(yearmap);

        res.put("ynjc",ynjc);


        /**
         *  用电功率检测
         */
        //1.查询最大时间
        String time = gutianAppMapper.getMaxTime(buildingId);
        //2.查询当前功率
        List<Map<String,Object>>  data = gutianAppMapper.getDqgl(buildingId,time);
        //3.查询上一时段功率
        List<Map<String,Object>> datasyd = gutianAppMapper.getSydgl(buildingId,time);
        //4.求比较率  顺便求总和
        Map<String,Map<String,Object>> dataMap = new HashMap<>();
        data.stream().forEach(map->{
            String buildingIdtmp = StrUtil.Obj2Str(map.get("building_id"));
            dataMap.put(buildingIdtmp,map);
        });

        List<Map<String,String>> ydgljc = new ArrayList<>();
        // 总电功率
         BigDecimal sum = BigDecimal.ZERO;
         // 总增长
         BigDecimal sumzz = BigDecimal.ZERO;
        // 上一时刻
        for (int i=0;i<datasyd.size();i++) {
            // 存储结果数据
            Map<String,String> tmp = new HashMap<>();
            // 默认值
            tmp.put("zz","-");

            Map<String,Object> map=datasyd.get(i);
            // 楼宇id
            String buildingIdtmp = StrUtil.Obj2Str(map.get("building_id"));
            // 楼宇名称
            String buildingName = StrUtil.Obj2Str(map.get("building_name"));

            // 上一时刻值
            BigDecimal activePower = StrUtil.Obj2Bigdecimal(map.get("active_power"));
            // 当前时刻楼宇信息
            Map<String,Object> now = dataMap.get(buildingIdtmp);
            // 当前时刻值
            BigDecimal activePowerNow = now == null ?  BigDecimal.ZERO:StrUtil.Obj2Bigdecimal(now.get("active_power"));
            // 求总电功率
            sum = sum.add(activePowerNow);

            //上一时刻不是0
            if (BigDecimal.ZERO.compareTo(activePower) != 0) {
              BigDecimal zz =   activePowerNow.subtract(activePower).divide(activePower,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
              tmp.put("zz",zz.toString());
              sumzz = sumzz.add(zz);
            }
            tmp.put("buildingName",buildingName);
            tmp.put("activePowerNow",activePowerNow.toString());

            ydgljc.add(tmp);
        }
        res.put("ydgljc",ydgljc);

        // 总用电功率
        Map<String,String> zydgl = new HashMap<>();
        zydgl.put("activePowerNow",sum.toString());
        zydgl.put("zz",sumzz.toString());
        res.put("zydgl",zydgl);
        return  res;
    }


    /**
     *  楼层级电气设备用能监测明细
     * @param elecUnitId
     * @param buildingGroup
     * @return
     */
    @Override
    public Map<String, Object> getLyqyjcmx(String elecUnitId, String buildingGroup) {

        Map<String,Object> res = new HashMap<>();

        // 用能检测
        List<Map<String,String>> ynjc = new ArrayList<>();
        //本周
        Map<String,String> weekmap = new HashMap<>();
        String[] week =StrUtil.calcStartAndEndTime("WEEK");
        // 总电量
        BigDecimal weeksum = gutianAppMapper.getSbYnjc(elecUnitId,week[0],week[1]);
        // 峰值电量
        BigDecimal weekfzdl = gutianAppMapper.getSbYnjcFzdl(elecUnitId,week[0],week[1]);
        weekmap.put("zqx","本周");
        weekmap.put("fzdl",StrUtil.BigDecimal2Str2(weekfzdl));
        weekmap.put("zdl",StrUtil.BigDecimal2Str2(weekfzdl));
        weekmap.put("gzdl","");
        if (weeksum != null && weekfzdl != null) {
            BigDecimal  gzdl =weeksum.subtract(weekfzdl);
            weekmap.put("gzdl",StrUtil.BigDecimal2Str2(gzdl));
        }
        //本月
        String[] month =StrUtil.calcStartAndEndTime("WEEK");
        Map<String,String> monthmap = new HashMap<>();
        // 总电量
        BigDecimal monthsum = gutianAppMapper.getSbYnjc(elecUnitId,month[0],month[1]);
        // 峰值电量
        BigDecimal monthfzdl = gutianAppMapper.getSbYnjcFzdl(elecUnitId,month[0],month[1]);
        monthmap.put("zqx","本月");
        monthmap.put("fzdl",StrUtil.BigDecimal2Str2(monthfzdl));
        monthmap.put("zdl",StrUtil.BigDecimal2Str2(monthsum));
        monthmap.put("gzdl","");
        if (monthsum != null && monthfzdl != null) {
            BigDecimal  gzdl =monthsum.subtract(monthfzdl);
            monthmap.put("gzdl",StrUtil.BigDecimal2Str2(gzdl));
        }

        //本年
        Map<String,String> yearmap = new HashMap<>();
        String[] year =StrUtil.calcStartAndEndTime("YEAR");
        // 总电量
        BigDecimal yearsum = gutianAppMapper.getSbYnjc(elecUnitId,year[0],year[1]);
        // 峰值电量
        BigDecimal yearfzdl = gutianAppMapper.getSbYnjcFzdl(elecUnitId,year[0],year[1]);
        yearmap.put("zqx","本年");
        yearmap.put("fzdl",StrUtil.BigDecimal2Str2(yearfzdl));
        yearmap.put("zdl",StrUtil.BigDecimal2Str2(yearsum));
        yearmap.put("gzdl","");
        if (yearsum != null && yearfzdl != null) {
            BigDecimal  gzdl =yearsum.subtract(yearfzdl);
            yearmap.put("gzdl",StrUtil.BigDecimal2Str2(gzdl));
        }

        ynjc.add(weekmap);
        ynjc.add(monthmap);
        ynjc.add(yearmap);

        res.put("ynjc",ynjc);

        // 楼层级设备检测详情
        //1.查询最大时间
        String time = gutianAppMapper.getSbMaxTime(buildingGroup);
        //2.查询当前功率
        Map<String,Object>  data = gutianAppMapper.getSbDqgl(buildingGroup,time);
        //3.查询上一时段功率
        Map<String,Object> datasyd = gutianAppMapper.getSbSydgl(buildingGroup,time);
        //4.求比较率  顺便求总和
        Map<String,String> ydgljc = new HashMap<>();
        if (data != null) {
            BigDecimal activePower =StrUtil.Obj2Bigdecimal(data.get("active_power"));
            ydgljc.put("buildingName",StrUtil.Obj2Str(data.get("building_name")));
            ydgljc.put("activePower",activePower.toString());
            if(datasyd != null) {
                BigDecimal activePowerold =StrUtil.Obj2Bigdecimal(datasyd.get("active_power"));
                if (BigDecimal.ZERO.compareTo(activePowerold) != 0) {
                    BigDecimal zz = activePower.subtract(activePowerold).divide(activePowerold,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    ydgljc.put("zz",zz.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                }
            }
        }

        // 用电功率监测
        res.put("ydgljc",ydgljc);
        return res;
    }

    /**
     * 用能-设备能耗
     * @param sceneId
     * @return
     */
    @Override
    public List<Map<String, Object>> getSbnh(String sceneId) {
        //
        List<Map<String, Object>> sbnh = gutianAppMapper.getSbnh(sceneId);
        //
        List<Map<String, Object>> online = gutianAppMapper.getSbOnline(sceneId);
        Map<String,String> onlineMap = new HashMap<>();
        online.stream().forEach(map->{
            onlineMap.put(StrUtil.Obj2Str(map.get("elec_unit_id")),"0");
        });

        sbnh.stream().forEach(map->{
            String elec_unit_id = StrUtil.Obj2Str(map.get("elec_unit_id"));
            if (onlineMap.containsKey(elec_unit_id)) {
                map.put("online","在线");
            } else {
                map.put("online","离线");
            }
        });

        return sbnh;
    }


    /**
     * app插入更新重大事件数据
     */
    public Map<String, Object> insertGTSZData(String sceneId, List<Map<String,Object>> list) {
        Map<String, Object> result = new HashMap<>(6);
        Map<String, Object> param = null;
        try {
            for (int i = 0;i<list.size();i++){
                param = list.get(i);
                int num = gutianAppMapper.selectReceptionnum(param,sceneId);
                if (num == 0){
                    gutianAppMapper.insertReception(param,sceneId);
                }else {
                    gutianAppMapper.updateReception(param,sceneId);
                }
            }
            result.put("result", "success");
        }catch (Exception e){
            log.error(e.getMessage(),e);
            result.put("result", "faile");
        }
        return result;
    }
}
