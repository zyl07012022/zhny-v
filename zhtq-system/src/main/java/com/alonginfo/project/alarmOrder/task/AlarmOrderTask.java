package com.alonginfo.project.alarmOrder.task;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.alarmOrder.service.TrWaringDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author ljg
 * @Date 2021/8/24 9:52
 */
@Configuration
@EnableScheduling
@EnableAsync
@Slf4j
public class AlarmOrderTask {

    @Autowired
    private TrWaringDataService trWaringDataService;

    @Scheduled(cron = "0 0/20 * * * ? ")
    public void task(){
        try {
            long startTime = System.currentTimeMillis();
            trWaringDataService.tranformerData();
            long endTime = System.currentTimeMillis();
            log.info("======变压器故障信息统计消耗"+(endTime-startTime)/1000+"毫秒");
        } catch (Exception e) {
            log.info("----------变压器故障信息统计失败--------------");
        }
    }
}
