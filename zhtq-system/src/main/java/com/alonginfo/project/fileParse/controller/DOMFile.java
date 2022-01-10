package com.alonginfo.project.fileParse.controller;

import com.alonginfo.project.fileParse.service.FADataService;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@MapperScan("com.alonginfo.project.fileParse.mapper")
public class DOMFile implements CommandLineRunner {

    @Autowired
    private FADataService faDataService;

    private Map<String,String> map2 ;
    @Value("${mxDataV.profile}")
    private String path; //文件上传位置  --服务器路径/home/ailang/updateLoad/


    @Override
    public  void run(String... strings) throws Exception{
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                WatchKey key;
                try {
                    WatchService watchService = FileSystems.getDefault().newWatchService();
                    Paths.get(path).register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
                    while (true) {
                        File file = new File(path);//path为监听文件夹
                        File[] files = file.listFiles();
                        System.out.println("等待新文件推送！");
                        key = watchService.take();//没有文件增加时，阻塞在这里
                        for (WatchEvent<?> event : key.pollEvents()) {
                            String fileName = path+event.context();
                            System.out.println("增加文件的文件夹路径"+fileName);
                            File file2 = new File(fileName);
                            Long filelength = file2.length();
                            byte[] filecontent = new byte[filelength.intValue()];
                            FileInputStream in = new FileInputStream(file2);
                            in.read(filecontent);
                            String str = new String(filecontent,"gbk");
                            org.dom4j.Document document =  DocumentHelper.parseText(str);
                            Element rootElement = document.getRootElement();
                            List<Element> list = rootElement.elements();
                            String str2 = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());
                            map2 = new HashMap<>();
                            Map<String,String> map =new HashMap<>();
                            saveElement(list,str2,map2,map);
                        }if (!key.reset()) {
                            break; //中断循环
                        }
                    }
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, 2000 , 3000);//第一个数字2000表示，2000ms以后开启定时器,第二个数字3000，表示3000ms后运行一次run
    }

    private void saveElement(List<Element> list,String str,Map<String,String> map2,Map<String,String> map){
        for(Element elementChild:list){
            //获取根节点下面的子节点;
                List<Element> subElementListchild = elementChild.elements();
                if (subElementListchild.size() > 0) {
                    saveElement(subElementListchild,str,map2,map);
                } else {
                    String name = elementChild.getQName().getName().trim();
                   String parentName =  elementChild.getParent().getQName().getName();
                    String text = elementChild.getText().trim();
                    if(parentName.equals("Subject")){
                        switch (name){
                            case "MessageTime" :
                                map.put("MessageTime",text);
                                break;
                        }
                    }if(parentName.equals("Describe")){
                        switch (name){
                            case "Accident_range" :
                                map.put("Accident_range",text);
                                break;
                            case "Content" :
                                map.put("Content",text);
                                break;
                            case "fault_type" :
                                map.put("fault_type",text);
                                map.put("DEVICE_ID",str);
                                map2.put("DEVICE_ID",str);
                                break;
                        }
                    }
                    else {
                        if(parentName.equals("TripDevice")) {
                            switch (name) {
                                case "mRID":
                                    map.put("mRID", text);
                                    break;
                                case "DevName":
                                    map.put("DevName", text);
                                    break;
                                case "PSRType":
                                    map.put("PSRType", text);
                                    break;
                            }
                        }else
                        if (parentName.equals("FaultSignal")){
                                map.put("TripTime", text);
                                faDataService.addFileInfo(map);
                        }else if(parentName.equals("Device")) {
                            String grandpaName =  elementChild.getParent().getParent().getQName().getName();
                            if (grandpaName.equals("FaultCurrent")) {
                                switch (name) {
                                    case "mRID":
                                        map2.put("mRID", text);
                                        break;
                                    case "DevName":
                                        map2.put("DevName", text);
                                        map2.put("parentName", "FaultCurrent");
                                        faDataService.addDeviceInfo(map2);
                                        break;
                                }
                            } else if (grandpaName.equals("FaultArea")) {
                                switch (name) {
                                    case "mRID":
                                        map2.put("mRID", text);
                                        break;
                                    case "DevName":
                                        map2.put("DevName", text);
                                        map2.put("parentName", "FaultArea");
                                        faDataService.addDeviceInfo(map2);
                                        break;
                                }
                            }
                        }
                    }
                    System.out.println("名字：" + name + "数据：" + text);
                }
        }
    }

}
