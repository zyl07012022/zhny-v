package com.alonginfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 *  程序启动
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MxApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(MxApplication.class, args);
        System.out.println("================抢修指挥后台服务-启动成功================ \n");
    }
}
