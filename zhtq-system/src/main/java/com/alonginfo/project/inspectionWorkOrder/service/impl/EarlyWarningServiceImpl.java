package com.alonginfo.project.inspectionWorkOrder.service.impl;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.aspectj.lang.annotation.DataSource;
import com.alonginfo.framework.aspectj.lang.enums.DataSourceType;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.component.entities.HeavyLoad;
import com.alonginfo.project.component.entities.LowVoltage;
import com.alonginfo.project.component.entities.NotBalance;
import com.alonginfo.project.emergencyCommand.entity.RelevanceCourts;
import com.alonginfo.project.emergencyCommand.entity.RelevanceUser;
import com.alonginfo.project.emergencyCommand.mapper.RepairWorkOrderMapper;
import com.alonginfo.project.inspectionWorkOrder.entity.CheckRepairOrder;
import com.alonginfo.project.inspectionWorkOrder.mapper.EarlyWarningMapper;
import com.alonginfo.project.inspectionWorkOrder.mapper.InspectionMapper;
import com.alonginfo.project.inspectionWorkOrder.service.EarlyWarningService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EarlyWarningServiceImpl implements EarlyWarningService {

    @Resource
    private EarlyWarningMapper earlyWarningMapper;
    

    @Resource
    private InspectionMapper inspectionMapper;

    @Resource
    private RepairWorkOrderMapper repairWorkOrderMapper;

    @Override
    public AjaxResult selectCompany() {
        List<Map<String, Object>> list = earlyWarningMapper.selectCompany();
        List<Map<String, Object>> data = new ArrayList<>();
        if (list != null && list.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            List<Map<String, Object>> childrenList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> childrenMap = new HashMap<>();
                String companyId = list.get(i).get("companyId") + "";
                if (!companyId.equals(map.get("value"))) {
                    map.put("value", companyId);
                    map.put("label", list.get(i).get("companyName") + "");
                }
                childrenMap.put("value", list.get(i).get("supplyId") + "");
                childrenMap.put("label", list.get(i).get("supplyName") + "");
                childrenList.add(childrenMap);
                if (i < list.size() - 1 && !String.valueOf(list.get(i + 1).get("companyId")).equals(companyId) || i == list.size() - 1) {
                    Map<String, Object> map2 = new HashMap<>();
                    List<Map<String, Object>> childrenList2 = new ArrayList<>();
                    childrenList2.addAll(childrenList);
                    map.put("children", childrenList2);
                    map2.putAll(map);
                    data.add(map2);
                    map.clear();
                    childrenList.clear();
                }
            }
        }
        return AjaxResult.success(data);
    }

    @Override
    public AjaxResult selectEarlyWarning(String itemType,String orgId) {
        Map<String, Object> data = new HashMap<>();
        String orgName ="";
        if(StringUtils.isNotEmpty(itemType)){
            if("04".equals(itemType)){
                orgName=" and county_company = '"+orgId+"'";
            }else if("05".equals(itemType)){
                orgName=" and power_supply = '"+orgId+"'";
            }else{
                orgName=orgId;
            }
        }
        List<Map<String, Object>> transIdfList = earlyWarningMapper.selectNumList(orgName);
        if(transIdfList.size()>0) {
            data.put("transfId", transIdfList);
        }else {
           String str [] = new String[3];
            data.put("transfId",str);
        }
        List<Map<String, Object>> abnormalList = earlyWarningMapper.selectAbnormal(orgName);
        if (StringUtils.isNotEmpty(abnormalList)) {
            abnormalList.forEach(str -> {
                data.put(str.get("code_name") + "", str.get("abnormalNum") + "");
            });
            if (data.get("pbzgz") == null) {
                data.put("pbzgz", 0);
            }
            if (data.get("sxbph") == null) {
                data.put("sxbph", 0);
            }
            if (data.get("dqdy") == null) {
                data.put("dqdy", 0);
            }
        }else {
                data.put("pbzgz", 0);
                data.put("sxbph", 0);
                data.put("dqdy", 0);
        }
        //查询台区经理数量
        List<Map<String, Object>> numList = earlyWarningMapper.selectNum(orgName);
        //查询台区数量
        data.put("courtsNumber", numList.get(0).get("courtsNumber"));
        //查询台区经理数量
        data.put("managerNumber", numList.get(0).get("managerNumber"));
        //查询用户数量
        data.put("userNumber", numList.get(0).get("userNumber"));
        //查询异常次数
        String str1 = "41";
        String str2 = "39";
        String str3 = "40";
        String date1 = "YEAR(updata_time)=YEAR(NOW())";//本年数据
        String date2 = "DATE_FORMAT(updata_time,'%Y-%m')=DATE_FORMAT(NOW(),'%Y-%m')";//本月数据
        String date3 = "DATE_FORMAT(updata_time,'%Y-%m')=DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH),'%Y-%m')";//上月数据
        String[] dates = {date1, date2, date3};
        String[] strs = {str1, str2, str3};
        Integer p = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                p++;
                List<Map<String, Object>> list2 = earlyWarningMapper.calculationStatistics(strs[i], dates[j], orgName);
                if (list2 != null && list2.size() > 0) {
                    Integer avg = 0;
                    avg = Integer.valueOf(list2.get(0).get("date") + "");
                    if (j == 2) {
                        if (avg != 0) {
                            Integer s =(Integer) data.get("data" + (p - 1)+"");
                            BigDecimal b = new BigDecimal(Math.round((float) 100 * (s - avg) / avg));
                            avg = Integer.valueOf(String.valueOf(b));
                        } else {
                            avg = 100;
                        }
                    }
                    data.put("data" + p + "", avg);
                } else {
                    Integer s = (Integer) data.get("data" + (p - 1)+"");
                    Integer b = 0;
                    if(p%3==0&&s.equals(b)){
                        data.put("data"+p+"",0);
                    }else if (p%3==0&&!s.equals(b)) {
                        data.put("data"+p+"",100);
                    }else {
                        data.put("data" + p + "", 0);
                    }
                }
            }
        }
        List<Map<String, String>> yearList = earlyWarningMapper.selectMonthList(orgName);
        List<Map<String, String>> monthList = earlyWarningMapper.selectDateList(orgName);
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int dom = cal.get(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < month; i++) {//年度异常统计
            String temp =String.valueOf(i+1);
            int count =0;
            int count2=0;
            Map<String,String> res=new LinkedHashMap<>();
            if(i<yearList.size()) {
                if (!temp.equals(StringUtils.valueOf(yearList.get(i).get("month")))) {
                    res.put("month",temp);
                    res.put("num","0");
                    yearList.add(i,res);
                }else{
                    count=i+1;
                    continue;
                }
                count=i+1;
            }else{
                for (int j = 0; j < count; j++) {
                    if (temp.equals(StringUtils.valueOf(yearList.get(j).get("month")))) {
                        count2++;
                    }
                }
                if(count2==0){
                    res.put("month",temp);
                    res.put("num","0");
                    yearList.add(i,res);
                }else{
                    continue;
                }

            }

        }
        for (int i = 0; i <dom ; i++) {
            String temp ="";
            if(i<9){
                temp="0"+(i+1);
            }else{
                temp=String.valueOf(i+1);
            }
            int count =0;
            int count2=0;
            Map<String,String> res=new LinkedHashMap<>();
            if(i<monthList.size()) {
                if (!temp.equals(monthList.get(i).get("today"))) {
                    res.put("num","0");
                    res.put("today",temp);
                    monthList.add(i, res);
                }else{
                    count=i+1;
                    continue;
                }
                count=i+1;
            }else{
                for (int j = 0; j < count; j++) {
                    if (temp.equals(monthList.get(j).get("today"))) {
                        count2++;
                    }
                }
                if(count2==0) {
                    res.put("num", "0");
                    res.put("today", temp);
                    monthList.add(i, res);
                }else{
                    continue;
                }
            }

        }
        data.put("yearList",yearList);
        data.put("monthList",monthList);
        return AjaxResult.success(data);
    }

    @Override
    public AjaxResult createWO(List<HashMap<String,Object>> list) {
        if(StringUtils.isNotEmpty(list)){
            String str = "YCXJ"+new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());
            int index = 0;
            for (HashMap map:list) {
                CheckRepairOrder checkRepairOrder = new CheckRepairOrder();
                if(index==0){
                    checkRepairOrder.setParentNumber("");
                    checkRepairOrder.setPatrolStatus("0");
                    checkRepairOrder.setPatrolSchemeNumber(str);
                }else if(index>0){
                    checkRepairOrder.setParentNumber(str);
                    Integer num = Integer.valueOf(String.valueOf(str.charAt(str.length()-1)))+1+index;
                    checkRepairOrder.setPatrolSchemeNumber(str.replace(str.charAt(str.length()-1)+"",String.valueOf(num)));
                    checkRepairOrder.setPatrolStatus("11");
                }
                checkRepairOrder.setPatrolSchemeName("异常预警巡检"+new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date()));
                checkRepairOrder.setPatrolType("2");
                checkRepairOrder.setVoltageClasses("7");
                checkRepairOrder.setSource("53");
                checkRepairOrder.setPatrolObject(StringUtils.valueOf(map.get("transf_name")));
                checkRepairOrder.setPatrolTypes("36");
                checkRepairOrder.setCountyCompany(StringUtils.valueOf(map.get("county_company2")));
                checkRepairOrder.setPowerSupply(StringUtils.valueOf(map.get("power_supply2")));
                checkRepairOrder.setSubordinateSubstation(StringUtils.valueOf(map.get("subordinate_substation")));
                checkRepairOrder.setFeeder(StringUtils.valueOf(map.get("feeder")));
                checkRepairOrder.setPatrolAbnormal("异常");
                String transfId =  StringUtils.valueOf(map.get("transf_id"));
                checkRepairOrder.setPointSite(StringUtils.valueOf(map.get("point_site")));
                try {
                    inspectionMapper.planAdd(checkRepairOrder);
                    String deviceType = "1";
                    String ID = StringUtils.valueOf(map.get("ID"));
                    earlyWarningMapper.upDeviceStatus(deviceType,ID);
                    if(StringUtils.isNotEmpty(checkRepairOrder.getUserIds())) {
                    RelevanceUser relevanceUser = new RelevanceUser();
                    relevanceUser.setUserId(StringUtils.valueOf(map.get("transf_id")));
                    relevanceUser.setWorkNumber(checkRepairOrder.getPatrolSchemeNumber());
                    repairWorkOrderMapper.addUser(relevanceUser);
                }
                    if(StringUtils.isNotEmpty(checkRepairOrder.getCourtsNumber())){
                            RelevanceCourts relevanceCourts = new RelevanceCourts();
                            relevanceCourts.setCourtsNumber(StringUtils.valueOf(map.get("tg_id")));
                            relevanceCourts.setWorkNumber(checkRepairOrder.getPatrolSchemeNumber());
                            repairWorkOrderMapper.addCourts(relevanceCourts);
                        }
                    if(checkRepairOrder.getPatrolAbnormal().equals("39")){//台区低压
                        LowVoltage lowVoltage = new LowVoltage();
                        lowVoltage.setPatrolSchemeNumber(checkRepairOrder.getPatrolSchemeNumber());
                        lowVoltage.setMinv(StringUtils.valueOf(map.get("min_v")));
                        lowVoltage.setTimeMinimumImbalance(StringUtils.valueOf(map.get("min_volt_time")));
                        lowVoltage.setLoadRatea(StringUtils.valueOf(map.get("volt_rate_a")));
                        lowVoltage.setLoadRatea(StringUtils.valueOf(map.get("volt_rate_b")));
                        lowVoltage.setLoadRatea(StringUtils.valueOf(map.get("volt_rate_c")));
                        inspectionMapper.addVoltageDetail(lowVoltage);
                    }else if(checkRepairOrder.getPatrolAbnormal().equals("40")){//三相不平衡
                        NotBalance notBalance = new NotBalance();
                        notBalance.setPatrolSchemeNumber(checkRepairOrder.getPatrolSchemeNumber());
                        notBalance.setTrigonalImbalance(StringUtils.valueOf(map.get("trigonal_imbalance")));
                        notBalance.setNormal(StringUtils.valueOf(map.get("trigonal_imbalance")));
                        notBalance.setMaxImbalance(StringUtils.valueOf(map.get("max_imbalance")));
                        notBalance.setTimeMaximumImbalance(StringUtils.valueOf(map.get("time_maximum_imbalance")));
                        notBalance.setLoadRatea(StringUtils.valueOf(map.get("load_rate_a")));
                        notBalance.setLoadRatea(StringUtils.valueOf(map.get("load_rate_b")));
                        notBalance.setLoadRatea(StringUtils.valueOf(map.get("load_rate_c")));
                        inspectionMapper.addTrigonalImbalance(notBalance);
                    }else if(checkRepairOrder.getPatrolAbnormal().equals("41")){//配变重过载
                        HeavyLoad heavyLoad = new HeavyLoad();
                        heavyLoad.setPatrolSchemeNumber(checkRepairOrder.getPatrolSchemeNumber());
                        heavyLoad.setHeavyLoad(StringUtils.valueOf(map.get("heavy_load")));
                        heavyLoad.setOverload(StringUtils.valueOf(map.get("overload")));
                        heavyLoad.setNormal(StringUtils.valueOf(map.get("normal")));
                        heavyLoad.setMaxImbalance(StringUtils.valueOf(map.get("max_imbalance")));
                        heavyLoad.setTimeMaximumImbalance(StringUtils.valueOf(map.get("time_maximum_imbalance")));
                        heavyLoad.setLoadRatea(StringUtils.valueOf(map.get("load_rate_a")));
                        heavyLoad.setLoadRatea(StringUtils.valueOf(map.get("load_rate_b")));
                        heavyLoad.setLoadRatea(StringUtils.valueOf(map.get("load_rate_c")));
                        inspectionMapper.addOverloadDetail(heavyLoad);
                    }
                    index++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

//        String str2 = UUID.randomUUID().toString().replace("-", "");
//        StringBuffer str = new StringBuffer("YCXJ");
//        String num = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());
//        num = str + num;
//        CheckRepairOrder checkRepairOrder = new CheckRepairOrder();
//        checkRepairOrder.setGuid(str2);
//        checkRepairOrder.setPatrolSchemeNumber(num);
//        checkRepairOrder.setPatrolSchemeName("异常巡检");
//        checkRepairOrder.setPatrolType("2");
//        checkRepairOrder.setVoltageClasses("7");
//        checkRepairOrder.setSource("53");
//        checkRepairOrder.setCountyCompany(StringUtils.valueOf(map.get("county_company")));
//        checkRepairOrder.setPowerSupply(StringUtils.valueOf(map.get("power_supply2")));
//        checkRepairOrder.setSubordinateSubstation(StringUtils.valueOf(map.get("subordinate_substation")));
//        checkRepairOrder.setFeeder(StringUtils.valueOf(map.get("feeder")));
//        checkRepairOrder.setPatrolAbnormal(StringUtils.valueOf(map.get("patrol_abnormal")));
//        String transfId =  StringUtils.valueOf(map.get("transf_id"));
//        checkRepairOrder.setPointSite(StringUtils.valueOf(map.get("point_site")));
//        try {
//            inspectionMapper.planAdd(checkRepairOrder);
//            if(StringUtils.isNotEmpty(checkRepairOrder.getUserIds())) {
//                String str1 = UUID.randomUUID().toString().replace("-", "");
//                RelevanceUser relevanceUser = new RelevanceUser();
//                relevanceUser.setUserId(StringUtils.valueOf(map.get("transf_id")));
//                relevanceUser.setWorkNumber(checkRepairOrder.getPatrolSchemeNumber());
//                relevanceUser.setGuid(str1);
//                repairWorkOrderMapper.addUser(relevanceUser);
//            }
//            if(StringUtils.isNotEmpty(checkRepairOrder.getCourtsNumber())){
//                    String str1 = UUID.randomUUID().toString().replace("-", "");
//                    RelevanceCourts relevanceCourts = new RelevanceCourts();
//                    relevanceCourts.setCourtsNumber(StringUtils.valueOf(map.get("transf_id")));
//                    relevanceCourts.setWorkNumber(checkRepairOrder.getPatrolSchemeNumber());
//                    relevanceCourts.setGuid(str1);
//                    repairWorkOrderMapper.addCourts(relevanceCourts);
//                }
//            if(StringUtils.isNotEmpty(checkRepairOrder.getPatrolAbnormal())){
//                if((checkRepairOrder.getPatrolAbnormal()).equals("39")){
//                    LowVoltage lowVoltage = new LowVoltage();
//                    lowVoltage.setGuid(num);
//                    lowVoltage.setPatrolSchemeNumber(checkRepairOrder.getPatrolSchemeNumber());
//                    inspectionMapper.addVoltageDetail(lowVoltage);
//                }else if((checkRepairOrder.getPatrolAbnormal()).equals("40")){
//                    NotBalance notBalance = new NotBalance();
//                    notBalance.setGuid(str2);
//                    notBalance.setPatrolSchemeNumber(checkRepairOrder.getPatrolSchemeNumber());
//                    inspectionMapper.addTrigonalImbalance(notBalance);
//                }else if((checkRepairOrder.getPatrolAbnormal()).equals("41")){
//                    HeavyLoad heavyLoad = new HeavyLoad();
//                    heavyLoad.setGuid(str2);
//                    heavyLoad.setPatrolSchemeNumber(checkRepairOrder.getPatrolSchemeNumber());
//                    inspectionMapper.addOverloadDetail(heavyLoad);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            earlyWarningMapper.deleteOrder(transfId);
//        }
        return AjaxResult.success();

    }

    @Override
    public AjaxResult selectAbnormalList(String itemType,String orgName,String patrolAbnormal) {
        if(StringUtils.isNotEmpty(itemType)){
            if("04".equals(itemType)){
                orgName=" and county_company = '"+orgName+"'";
            }else {
                orgName=" and power_supply = '"+orgName+"'";
            }
        }
        List<Map<String, String>> list = earlyWarningMapper.selectAbnormalList(orgName,patrolAbnormal);

        return AjaxResult.success(list);
    }

    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public String queryOrgName(String orgId) {
        return earlyWarningMapper.queryOrgName(orgId);
    }

    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public Integer selectNormalTr(String itemType,String orgId) {
        Integer num = 0 ;
        String str="C.SCREEN_NAME3";
        if("04".equals(itemType)){
            str = "C.SCREEN_NAME3";
        }else if("05".equals(itemType)){
            str = "C.SCREEN_NAME2";
        }else{
            orgId="";
        }
        num = earlyWarningMapper.selectNormalTr(str,orgId);
        return num;
    }


}
