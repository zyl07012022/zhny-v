package com.alonginfo.project.component.service.impl;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.aspectj.lang.annotation.DataSource;
import com.alonginfo.framework.aspectj.lang.enums.DataSourceType;
import com.alonginfo.project.component.entities.DropParams;
import com.alonginfo.project.component.mapper.DropMapper;
import com.alonginfo.project.component.service.DropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author ljg
 * @Date 2021/7/15 13:44
 */
@Service
@DataSource(DataSourceType.SLAVE)
public class DropServiceImpl implements DropService {

    @Autowired
    private DropMapper dropListMapper;


    @Override
    public List<DropParams> queryStList(String query) {
        return dropListMapper.queryStList(query);
    }

    @Override
    public List<DropParams> queryFeederList(DropParams dropParams) {
        return dropListMapper.queryFeederList(dropParams);
    }

    @Override
    public List<DropParams> queryDeviceList(Map map) {
        String trArea = "D5000.dms_tr_device";//配变台区
        String combined = "D5000.dms_combined_device";//开闭所/环网室
        String dlLine = "D5000.dms_section_device";//电缆通道/架空线路
        String dbDevice = "D5000.dms_cb_device";//柱上开关
        List<DropParams> list = new ArrayList<DropParams>();
        if ("配变台区".equals(String.valueOf(map.get("guid")))) {
            map.put("tableNo", trArea);
        } else if ("开闭所/环网室".equals(String.valueOf(map.get("guid")))) {
            map.put("tableNo", combined);
        } else if ("电缆通道/架空线路".equals(String.valueOf(map.get("guid")))) {
            map.put("tableNo", dlLine);
        } else if ("柱上开关".equals(String.valueOf(map.get("guid")))) {
            map.put("tableNo", dbDevice);
        } else {
            if (StringUtils.isEmpty(StringUtils.valueOf(map.get("guid")))) {
                map.put("tableNo", trArea);
                List<DropParams> list1 = dropListMapper.queryDeviceList(map);
                map.put("tableNo", combined);
                List<DropParams> list2 = dropListMapper.queryDeviceList(map);
                map.put("tableNo", dlLine);
                List<DropParams> list3 = dropListMapper.queryDeviceList(map);
                map.put("tableNo", dbDevice);
                List<DropParams> list4 = dropListMapper.queryDeviceList(map);
                list.addAll(list1);
                list.addAll(list2);
                list.addAll(list3);
                list.addAll(list4);
            } else {
                String[] arr = StringUtils.valueOf(map.get("guid")).split("//");
                for (int i = 0; i < arr.length; i++) {
                    if ("配变台区".equals(arr[i])) {
                        map.put("tableNo", trArea);
                        List<DropParams> list1 = dropListMapper.queryDeviceList(map);
                        list.addAll(list1);
                    }
                    if ("开闭所/环网室".equals(arr[i])) {
                        map.put("tableNo", combined);
                        List<DropParams>  list2 = dropListMapper.queryDeviceList(map);
                        list.addAll(list2);
                    }
                    if ("电缆通道/架空线路".equals(arr[i])) {
                        map.put("tableNo", dlLine);
                        List<DropParams>  list3 = dropListMapper.queryDeviceList(map);
                        list.addAll(list3);
                    }
                    if ("柱上开关".equals(arr[i])) {
                        map.put("tableNo", dbDevice);
                        List<DropParams> list4 = dropListMapper.queryDeviceList(map);
                        list.addAll(list4);
                    }
                }
                return list;
            }
        }
        return dropListMapper.queryDeviceList(map);
    }

    @Override
    public List<Map<String, Object>> queryOrgId() {
        LinkedHashMap<String, Object> map = dropListMapper.queryOrgId();
        if (map.containsKey("ID")) {
            List<Map<String, Object>> list1 = dropListMapper.queryParentId(StringUtils.valueOf(map.get("ID")));
            map.put("children", list1);
            for (Map<String, Object> res : list1) {
                if (map.containsKey("ID")) {
                    List<Map<String, Object>> list2 = dropListMapper.queryParentId(StringUtils.valueOf(res.get("ID")));
                    res.put("children", list2);
                }
            }
        }
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map);
        return list;
    }

    public static void main(String[] args) {
        String str = "//";
        String[] str1 = str.split("//");
        for (int i = 0; i <str1.length ; i++) {
            System.out.println("结果"+i+str1[i]);
        }
    }
}
