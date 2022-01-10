package com.alonginfo.project.alarmOrder.service.impl;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.aspectj.lang.annotation.DataSource;
import com.alonginfo.framework.aspectj.lang.enums.DataSourceType;
import com.alonginfo.framework.config.RedisConfig;
import com.alonginfo.project.alarmOrder.entity.YcyjDevice;
import com.alonginfo.project.alarmOrder.mapper.AlarmOrderMapper;
import com.alonginfo.project.alarmOrder.mapper.TrWaringDataMapper;
import com.alonginfo.project.alarmOrder.service.TrWaringDataService;
import com.alonginfo.project.alarmOrder.vo.Params;
import com.alonginfo.utils.NumUtil;
import com.alonginfo.utils.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author ljg
 * @Date 2021/7/14 17:13
 */
@Service
public class TrWaringDataServiceImpl implements TrWaringDataService {
    static Jedis jedis;
    final static int gzfz = 120;
    final static int sxfz = 120;
    final static int dyfz = 60;

    @Autowired
    private TrWaringDataMapper trWaringDataMapper;

    @Autowired
    private AlarmOrderMapper alarmOrderMapper;

    @Autowired
    RedisConfig redisConfig;



    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public void tranformerData() {
        //连接本地的redis服务
        jedis = new Jedis("127.0.0.1", 6379);
        System.out.println("Redis连接成功");
        //查看服务是否运行
        System.out.println(jedis.ping());
        List<Map<String, Object>> sxList = new ArrayList<Map<String, Object>>();//三相不平衡集合
        List<Map<String, Object>> dyList = new ArrayList<Map<String, Object>>();//台区低压集合
        List<Map<String, Object>> gzList = new ArrayList<Map<String, Object>>();//配变重过载集合
        List<Map<String, Object>> list = trWaringDataMapper.queryTrList();
        for (Map params : list) {
            String transf_id = StringUtils.valueOf(params.get("TRANSF_ID"));//变压器id
            String transf_name = StringUtils.valueOf(params.get("TRANSF_NAME"));//变压器名称
            String feeder_id = StringUtils.valueOf(params.get("FEEDER_ID"));//馈线id
            String feeder_name = StringUtils.valueOf(params.get("FEEDER_NAME"));//馈线名称
            String power_company = StringUtils.valueOf(params.get("POWER_COMPANY"));//所属供电公司
            String power_supply = StringUtils.valueOf(params.get("POWER_SUPPLY"));//所属供电所
            Double pa = valueOf(params.get("PA"));//A相功率
            Double pb = valueOf(params.get("PB"));//B相功率
            Double pc = valueOf(params.get("PC"));//C相功率
            Double rated_capacity = valueOf(params.get("RATED_CAPACITY"));//额定容量
            Double voltA = valueOf(params.get("UA"));//A相电压
            Double voltB = valueOf(params.get("UB"));//B相电压
            Double voltC = valueOf(params.get("UC"));//C相电压
            Double ia = valueOf(params.get("IA"));//A相电流
            Double ib = valueOf(params.get("IB"));//B相电流
            Double ic = valueOf(params.get("IC"));//C相电流
            Double p3 = valueOf(params.get("P3"));//三相有功功率
            Double q3 = valueOf(params.get("Q3"));//三相无功功率
            String tg_id = "";//台区编号
            String tg_name = "";//台区名称
            String areaPM_Name = "陈胜滨";
            String user_num = "224";
            int durTime = 0;//持续时间
            /**
             * maxI 最大相电流
             * minI 最小相电流
             */
            Double maxI = (ia > ib) ? ia : ib;
            maxI = (maxI > ic) ? maxI : ic;
            Double minI = (ia < ib) ? ia : ib;
            maxI = (minI < ic) ? minI : ic;
            Double pbLoad = p3 / rated_capacity * 100;//负载率
            Double bphd = (maxI - minI) / maxI * 100;//三相负荷不平衡度
            Double[] dyArr = {voltA, voltB, voltC};
            Boolean dyQk = dyFlage(dyArr);
            /**
             * 异常类型 39台区低压  40三相不平衡 41配变重过载
             */
            /**配变重过载*/
            if (pbLoad >= 80) {
                Map<String, Object> gzMap = new LinkedHashMap<String, Object>();
                if (pbLoad <= 100) {
                    gzMap.put("error_type", "重载");
                } else {
                    gzMap.put("error_type", "过载");
                }
                gzMap.put("transf_id", transf_id);
                gzMap.put("transf_name", transf_name);
                gzMap.put("feeder_id", feeder_id);
                gzMap.put("feeder_name", feeder_name);
                gzMap.put("power_company", power_company);
                gzMap.put("power_supply", power_supply);
                gzMap.put("patrol_abnormal", "41");
                gzMap.put("tg_id", tg_id);
                gzMap.put("tg_name", tg_name);
                gzMap.put("tg_id", tg_id);
                gzMap.put("areaPM_Name", areaPM_Name);
                gzMap.put("user_num", user_num);
                gzMap.put("durTime", durTime);
                if (StringUtils.isNotEmpty((List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("gzList".getBytes())))) {
                    gzList = (List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("gzList".getBytes()));
                    if (StringUtils.isNotEmpty(gzList)) {
                        for (Map map : gzList) {
                            if (transf_id.equals(StringUtils.valueOf(map.get("transf_id")))) {
                                map.put("durTime", Integer.valueOf(StringUtils.valueOf(map.get("durTime"))) + 20);
                            } else {
                                gzList.add(gzMap);
                            }
                            removElement(gzList, gzfz);
                        }
                    } else {
                        gzList.add(gzMap);
                    }
                } else {
                    jedis.set("gzList".getBytes(), SerializeUtil.serialize(gzList));//set
                }
            } else {
                if (StringUtils.isNotEmpty((List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("gzList".getBytes())))) {
                    gzList = (List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("gzList".getBytes()));
                    removElement2(gzList, transf_id);
                }
            }
            /**三相不平衡*/
            if (bphd > 25 && pbLoad > 50) {
                Map<String, Object> sxMap = new LinkedHashMap<String, Object>();
                sxMap.put("error_type", "三相不平衡");
                sxMap.put("transf_id", transf_id);
                sxMap.put("transf_name", transf_name);
                sxMap.put("feeder_id", feeder_id);
                sxMap.put("feeder_name", feeder_name);
                sxMap.put("power_company", power_company);
                sxMap.put("power_supply", power_supply);
                sxMap.put("patrol_abnormal", "40");
                sxMap.put("tg_id", tg_id);
                sxMap.put("tg_name", tg_name);
                sxMap.put("areaPM_Name", areaPM_Name);
                sxMap.put("user_num", user_num);
                sxMap.put("durTime", durTime);
                if (StringUtils.isNotEmpty((List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("sxList".getBytes())))) {
                    sxList = (List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("sxList".getBytes()));
                    if (StringUtils.isNotEmpty(sxList)) {
                        for (Map map : sxList) {
                            if (transf_id.equals(StringUtils.valueOf(map.get("transf_id")))) {
                                map.put("durTime", Integer.valueOf(StringUtils.valueOf(map.get("durTime"))) + 20);
                            } else {
                                sxList.add(sxMap);
                            }
                            removElement(sxList, sxfz);
                        }
                    } else {
                        sxList.add(sxMap);
                    }
                } else {
                    jedis.set("sxList".getBytes(), SerializeUtil.serialize(sxList));//set
                }
            } else {
                if (StringUtils.isNotEmpty((List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("sxList".getBytes())))) {
                    sxList = (List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("sxList".getBytes()));
                    removElement2(gzList, transf_id);
                }
            }
            /**台区低压*/
            if (dyQk) {
                Map<String, Object> dyMap = new LinkedHashMap<String, Object>();
                dyMap.put("error_type", "低压");
                dyMap.put("transf_id", transf_id);
                dyMap.put("transf_name", transf_name);
                dyMap.put("feeder_id", feeder_id);
                dyMap.put("feeder_name", feeder_name);
                dyMap.put("power_company", power_company);
                dyMap.put("power_supply", power_supply);
                dyMap.put("patrol_abnormal", "39");
                dyMap.put("tg_id", tg_id);
                dyMap.put("tg_name", tg_name);
                dyMap.put("areaPM_Name", areaPM_Name);
                dyMap.put("user_num", user_num);
                dyMap.put("durTime", durTime);
                if (StringUtils.isNotEmpty((List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("dyList".getBytes())))) {
                    dyList = (List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("dyList".getBytes()));
                    if (StringUtils.isNotEmpty(dyList)) {
                        for (Map map : dyList) {
                            if (transf_id.equals(StringUtils.valueOf(map.get("transf_id")))) {
                                map.put("durTime", Integer.valueOf(StringUtils.valueOf(map.get("durTime"))) + 20);
                            } else {
                                dyList.add(dyMap);
                            }
                            removElement(dyList, dyfz);
                        }
                    } else {
                        sxList.add(dyMap);
                    }
                } else {
                    jedis.set("dyList".getBytes(), SerializeUtil.serialize(dyList));//set
                }
            } else {
                if (StringUtils.isNotEmpty((List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("dyList".getBytes())))) {
                    dyList = (List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("dyList".getBytes()));
                    removElement2(gzList, transf_id);
                }
            }
        }
    }

    private void insertByq(Map map) {
        alarmOrderMapper.insertYcStatus(map);
    }

    private List<Map<String, Object>> removElement(List<Map<String, Object>> list, int fz) {
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> row = it.next();
            if (Integer.parseInt(StringUtils.valueOf(row.get("durTime"))) > fz) {
                row.put("updata_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                insertByq(row);
                it.remove();
            }
        }
        return list;
    }

    private List<Map<String, Object>> removElement2(List<Map<String, Object>> list, String id) {
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> row = it.next();
            if (id.equals(Integer.parseInt(StringUtils.valueOf(row.get("transf_id"))))) {
                it.remove();
            }
        }
        return list;
    }

    private Boolean dyFlage(Double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] - 220) / 220.00 * 100 > 10) {
                return true;
            }
        }
        return false;
    }

    private Double valueOf(Object obj) {
        obj = StringUtils.valueOf(obj);
        if ("".equals(obj)) {
            obj = "-1";
            return Double.parseDouble((String) obj);
        } else {
            return Double.parseDouble((String) obj);
        }

    }

    public static void main(String[] args) {
        List<Map> list = new ArrayList<>();
//        Map map = new HashMap();
//        map.put("1", 1);
//        map.put("2", 2);
//        map.put("3", 3);
//        list.add(map);
        System.out.println("test ok");

        //连接本地的redis服务
        jedis = new Jedis("127.0.0.1", 6379);
        System.out.println("连接成功");
        //查看服务是否运行
        if (StringUtils.isEmpty((List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("key".getBytes())))) {
            System.out.println("成功了");
        }
        System.out.println(jedis.ping());
        //删除Redis的缓存
        jedis.flushAll();

        //字符串
        jedis.set("key".getBytes(), SerializeUtil.serialize(list));//set
        List<Map<String, Object>> list1 = (List<Map<String, Object>>) SerializeUtil.unserialize(jedis.get("key".getBytes()));
        for (Map<String, Object> res : list1) {
            System.out.println(res);

        }

    }


}
