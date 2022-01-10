package com.alonginfo.project.agriculture.service.impl;

import com.alonginfo.project.agriculture.mapper.AppAgricultureMapper;
import com.alonginfo.project.agriculture.service.IAppAgricultureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * app农业场景实现层
 * @author sdj
 * @date 2020年5月8日11:47:44
 */
@Service
public class AppAgricultureServiceImpl implements IAppAgricultureService {

    @Autowired
    private AppAgricultureMapper mapper;

    /**
     * 查询所有监测点及最近一条数据
     * @param sceneName
     * @return
     */
    @Override
    public List<Map<String, Object>> selectWatch(String sceneName) {
        List<Map<String, Object>> resList = null;
        if ("plant".equals(sceneName)) {// 石斛场景
            resList = mapper.selectPlantWatch();
        } else if ("chicken".equals(sceneName)) {// 养殖场景
            resList = mapper.selectChickenWatch();
        }
        return Optional.ofNullable(resList).orElse(new ArrayList<Map<String, Object>>());
    }

    /**
     * 根据监测点id获取页面详情
     * @param watchId
     * @return
     */
    @Override
    public Map<String, Object> getWatchForId(String watchId,String sceneName) {
        Map<String, Object> resMap = null;
        if ("plant".equals(sceneName)) {// 石斛场景
            resMap = mapper.getPlantWatchForId(watchId);
        } else if ("chicken".equals(sceneName)) {// 养殖场景
            resMap = mapper.getChickenWatchForId(watchId);
        }
        return Optional.ofNullable(resMap).orElse(new HashMap<String, Object>());
    }

    /**
     * 获取异常检测点
     * @param sceneName
     * @return
     */
    @Override
    public List<Map<String, Object>> getWaringWatch(String sceneName) {
        // 检测点id为空的查询所有检测点数据
        String watchId = "";

        // 所有监测点结果集
        List<Map<String, Object>> resList = null;
        if ("plant".equals(sceneName)) {// 石斛场景
            resList = mapper.selectPlantWaringWatch();

        } else if ("chicken".equals(sceneName)) {// 养殖场景
            resList = mapper.selectChickenWaringWatch();
        }
        // 告警结果集
        List<Map<String, Object>> waringList = Optional.ofNullable(resList).orElse(new ArrayList<Map<String, Object>>())
                .stream().filter(map -> Float.parseFloat(Optional.ofNullable(String.valueOf(map.get("coT"))).orElse("0")) > Float.parseFloat(String.valueOf(map.get("coTMax")))
                        ||  Float.parseFloat(String.valueOf(Optional.ofNullable(map.get("sd")).orElse("0"))) > Float.parseFloat(String.valueOf(map.get("sdMax")))
                        ||  Float.parseFloat(String.valueOf(Optional.ofNullable(map.get("sd")).orElse("0"))) < Float.parseFloat(String.valueOf(map.get("sdMin")))
                        ||  Float.parseFloat(String.valueOf(Optional.ofNullable(map.get("wd")).orElse("0"))) > Float.parseFloat(String.valueOf(map.get("wdMax")))
                        ||  Float.parseFloat(String.valueOf(Optional.ofNullable(map.get("wd")).orElse("0"))) < Float.parseFloat(String.valueOf(map.get("wdMin")))
                ).collect(Collectors.toList());
        return waringList;
    }

    /**
     * 查询一个月设备能耗
     *
     * @param baseType
     * @param rows
     * @return
     */
    @Override
    public List<Map<String, Object>> getUnitPower(String baseType, Integer rows) {
        return mapper.selectUnitPower(baseType, rows);
    }

    /**
     * 查询house近况 --首页
     *
     * @param baseType
     * @param rows
     * @return
     */
    @Override
    public List<Map<String, Object>> getHouseSurvey(String baseType, Integer rows) {
        return mapper.selectHouseSurvey(baseType, rows);
    }

    /**
     * 增加产出
     *
     * @param baseType
     * @param outputList
     * @return
     */
    @Override
    @Transactional
    public Integer addOutput(String baseType, List<Map<String, Object>> outputList) {
        if ("1".equals(baseType)) {
            //先删除同样的鸡舍同样的日期
            mapper.deleteChickenhouseOutput(outputList);
            return mapper.insertChickenhouseOutput(outputList);
        } else if ("2".equals(baseType)) {
            mapper.deleteGreenhouseOutput(outputList);
            return mapper.insertGreenhouseOutput(outputList);
        }
        return null;
    }
}
