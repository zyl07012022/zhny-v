package com.alonginfo.project.alarmOrder.service.impl;

import com.alonginfo.project.alarmOrder.entity.DeviceStatus;
import com.alonginfo.project.alarmOrder.enums.PATROL_ABNORMAL;
import com.alonginfo.project.alarmOrder.enums.Status;
import com.alonginfo.project.alarmOrder.mapper.AlarmOrderMapper;
import com.alonginfo.project.alarmOrder.service.AlarmOrderService;
import com.alonginfo.project.alarmOrder.vo.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 崔亚魁
 */
@Service
public class AlarmOrderServiceImpl implements AlarmOrderService {

    @Autowired
    private AlarmOrderMapper alarmOrderMapper;


    @Override
    public Map<String,Object> calculate(Params params){

        String transf_id = params.getTransf_id();

        Double pa=params.getPa();
        Double pb=params.getPb();
        Double pc=params.getPc();
        Double rated_capacity= params.getRated_capacity();
        Double maxI=params.getMaxI();
        Double minI=params.getMinI();
        Double voltA=params.getUa();
        Double voltB=params.getUb();
        Double voltC=params.getUc();
        Double ia = params.getIa();
        Double ib = params.getIb();
        Double ic = params.getIc();
        int durTime=params.getDurTime();

        DeviceStatus deviceStatus = new DeviceStatus();
        deviceStatus.setTransf_id(transf_id);

        //三项最大负载率
        Map<String, Object> maxLoad = alarmOrderMapper.selectMaxLoad(transf_id);
        if (maxLoad!=null) {
            deviceStatus.setLoad_rate_a((Double) maxLoad.get("MAXFZA"));
            deviceStatus.setLoad_rate_b((Double)maxLoad.get("MAXFZB"));
            deviceStatus.setLoad_rate_c((Double)maxLoad.get("MAXFZC"));
        }

        //三项不平衡/平衡时间
        Map<String,Object> unbalanceTime = alarmOrderMapper.selectUnbalanceTime(transf_id);
        if (unbalanceTime != null) {
            deviceStatus.setTrigonal_imbalance((Double) unbalanceTime.get("bphtime"));
            deviceStatus.setTrigonal_nomal((Double) unbalanceTime.get("phtime"));
        }

        //最大不平衡度/时间
        Map<String, Object> maxUnbalance = alarmOrderMapper.selectMaxUnbalance(transf_id);

        if (maxUnbalance!=null) {
            deviceStatus.setMax_imbalance((Double) maxUnbalance.get("MAXBPH"));
            deviceStatus.setTime_maximum_imbalance((Timestamp) maxUnbalance.get("UPDATE_TIME"));
        }

        //最低电压及时刻
        Map<String, Object> minVolt = alarmOrderMapper.selectMinVolt(transf_id);
        if (minVolt!=null) {
            deviceStatus.setMin_v((Double)minVolt.get("MIN_U"));
            deviceStatus.setMin_volt_time((Timestamp)minVolt.get("UPDATE_TIME"));
        }

        //过载/重载/正常时间
        Map<String, Object> loadTime = alarmOrderMapper.selectLoadTime(transf_id);

        if (loadTime != null) {
            deviceStatus.setOverload((Double)loadTime.get("GZTIME"));
            deviceStatus.setHeavy_load((Double)loadTime.get("CZTIME"));
            deviceStatus.setNormal((Double)loadTime.get("ZCTIME"));
        }

        //查询当天低电压时间
        Map<String, Object> lowVoltTime = alarmOrderMapper.selectLowVoltCnt(transf_id);

        Map<String, Object> highVoltTime = alarmOrderMapper.selectHighVoltCnt(transf_id);

        Double lowA = (Double)lowVoltTime.get("lowVoltA");
        Double lowB = (Double)lowVoltTime.get("lowVoltB");
        Double lowC = (Double)lowVoltTime.get("lowVoltC");

        Double highA = (Double)highVoltTime.get("highVoltA");
        Double highB = (Double)highVoltTime.get("highVoltB");
        Double highC = (Double)highVoltTime.get("highVoltC");

        Double percentA = division((lowA+highA),24,2);
        Double percentB = division((lowB+highB),24,2);
        Double percentC = division((lowC+highC),24,2);

        deviceStatus.setVolt_rate_a(percentA);
        deviceStatus.setVolt_rate_b(percentB);
        deviceStatus.setVolt_rate_c(percentC);

        Map<String, Object> resultMap = new HashMap<>();

        //配变负载率
        Double loadRate= 0.00;

        if (rated_capacity!=null&&rated_capacity.intValue()!=0){
            loadRate=division((pa+pb+pc),rated_capacity,2);

        }
        //负载状态 0=正常 1= 过载 2 = 重载
        int loadStatus = 0;

        loadStatus =judgeLoadStatus(loadRate,durTime);

        if (loadStatus==1){
            deviceStatus.setPatrol_abnormal(PATROL_ABNORMAL.OVERLOAD.getCode());
            deviceStatus.setError_type(Status.OVER_LOAD.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        } else if (loadStatus==2) {
            deviceStatus.setPatrol_abnormal(PATROL_ABNORMAL.OVERLOAD.getCode());
            deviceStatus.setError_type(Status.HEAVY_LOAD.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }

        //三项不平衡率
        Double unbalanceRate =0.00;
        //三项不平衡状态 0=正常 9= 三项不平衡

        if (maxI!=null&&maxI.intValue()!=0){
            unbalanceRate = division((maxI-minI),maxI,2);
            if (unbalanceRate>0.25 && (loadRate>0.5)){
                deviceStatus.setPatrol_abnormal(PATROL_ABNORMAL.UNBLANCE.getCode());
                deviceStatus.setError_type(Status.UNBALANCE.getCode());
                alarmOrderMapper.saveErrorStatus(deviceStatus);
            }
        }

        //A相电压状态 0=正常 1=低压 2=高压
        int voltAStatus=0;
        int voltBStatus=0;
        int voltCStatus=0;

        //判断A相低、高压
        if (voltA!=null){
            voltAStatus =judgeVoltStatus(voltA,durTime);
        }

        if (voltAStatus==1) {
            deviceStatus.setPatrol_abnormal(PATROL_ABNORMAL.TG_LOWVOLT.getCode());
            deviceStatus.setError_type(Status.LOW_VOLTA.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        } else if (voltAStatus==2) {
            deviceStatus.setPatrol_abnormal(PATROL_ABNORMAL.TG_LOWVOLT.getCode());
            deviceStatus.setError_type(Status.HIGH_VOLTA.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }

        //判断B相低、高压
        if (voltB!=null){
            voltBStatus =judgeVoltStatus(voltB,durTime);
        }

        if (voltBStatus==1) {
            deviceStatus.setPatrol_abnormal(PATROL_ABNORMAL.TG_LOWVOLT.getCode());
            deviceStatus.setError_type(Status.HIGH_VOLTB.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        } else if (voltBStatus==2) {
            deviceStatus.setPatrol_abnormal(PATROL_ABNORMAL.TG_LOWVOLT.getCode());
            deviceStatus.setError_type(Status.HIGH_VOLTC.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }

        //判断C相低、高压
        if (voltC!=null){
            voltCStatus =judgeVoltStatus(voltC,durTime);
        }
        if (voltCStatus==1) {
            deviceStatus.setPatrol_abnormal(PATROL_ABNORMAL.TG_LOWVOLT.getCode());
            deviceStatus.setError_type(Status.LOW_VOLTC.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        } else if (voltCStatus==2) {
            deviceStatus.setPatrol_abnormal(PATROL_ABNORMAL.TG_LOWVOLT.getCode());
            deviceStatus.setError_type(Status.HIGH_VOLTC.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }

        deviceStatus.setPatrol_abnormal(null);
        //判断电压数据是否异常
        if (voltA>264 || voltA<132 ) {
            deviceStatus.setError_type(Status.UA_ERROR.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }

        if (voltB>264 || voltB<132 ) {
            deviceStatus.setError_type(Status.UB_ERROR.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }

        if (voltC>264 || voltC<132 ) {
            deviceStatus.setError_type(Status.UC_ERROR.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }

        //判断功率数据是否异常
        if (pa>3*rated_capacity) {
            deviceStatus.setError_type(Status.PA_ERROR.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }

        if (pb>3*rated_capacity) {
            deviceStatus.setError_type(Status.PB_ERROR.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }

        if (pb>3*rated_capacity) {
            deviceStatus.setError_type(Status.PC_ERROR.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }

        //判断电流数据是否异常
        Double capacityI=10000.0;

        if (ia>3*capacityI) {
            deviceStatus.setError_type(Status.IA_ERROR.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }
        if (ib>3*capacityI) {
            deviceStatus.setError_type(Status.IB_ERROR.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }
        if (ic>3*capacityI) {
            deviceStatus.setError_type(Status.IC_ERROR.getCode());
            alarmOrderMapper.saveErrorStatus(deviceStatus);
        }
        return resultMap;
    }


    /**
     * 判断电压状态 1= 低电压 2 = 高电压
     */
    private int judgeVoltStatus(Double volt,int durTime){
        int voltStatus =0;

        if (volt<220){
            if ((220-volt)/220>0.1&&durTime>=60){
                //低电压
                voltStatus=1;
            }
        }else {
            if ((volt-220)/220>0.07&&durTime>=60){
                //高电压
                voltStatus=2;
            }
        }
        return  voltStatus;
    }

    /**
     * 判断负载状态 1= 过载 2 = 重载
     */
    private int judgeLoadStatus(Double loadRate,int durTime){

        int loadStatus=0;

        if (loadRate>1&&durTime>=120){
            loadStatus = 1;
        }else if(loadRate>=0.8 && loadRate<=1 && durTime>=120){
            loadStatus = 2;
        }
        return loadStatus;
    }

    public static double division(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


}
