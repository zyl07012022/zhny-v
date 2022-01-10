package com.alonginfo.project.inspectionApp.controller;

import com.alibaba.fastjson.JSON;
import com.alonginfo.common.utils.IdUtils;
import com.alonginfo.common.utils.file.FileUtils;
import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.inspectionApp.service.InspectionAppService;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/inspectionApp")
public class InspectionAppController extends BaseController {

    private final ResourceLoader resourceLoader;

    @Resource
    private InspectionAppService inspectionAppService;


    @Autowired
    public InspectionAppController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Value("${mxDataV.imgPath}")
    private String path;

    /**
     * 查询巡检列表
     *
     * @return
     */
    @PostMapping("/selectInspection")
    public AjaxResult selectInspection(@RequestParam(value = "patrolSendData", required = false) String patrolSendData) {
        return inspectionAppService.selectInspection(patrolSendData);
    }

    @RequestMapping(value = "/upload_img",method = {RequestMethod.GET, RequestMethod.POST},produces = "application/json; charset=utf-8")
    public Map<String,String> upload_img(MultipartFile file, HttpServletRequest request) throws Exception {
//        String path = "/home/ailang/updateLoad/img/";
        //创建文件
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String id = IdUtils.simpleUUID();
        String fileName = file.getOriginalFilename();
        String img = id + fileName.substring(fileName.lastIndexOf("."));//.jpg
        File destFile = new File(dir, img);
        FileOutputStream imgOut = new FileOutputStream(destFile);//根据 dir 抽象路径名和 img 路径名字符串创建一个新 File 实例。
        /* System.out.println(file.getBytes());*/
        imgOut.write(file.getBytes());//返回一个字节数组文件的内容
        BufferedImage image = new BufferedImage(400, 400,BufferedImage.TYPE_INT_RGB );
        image.getGraphics().drawImage(ImageIO.read(destFile), 0, 0, 400, 400, null); // 绘制缩小后的图
        imgOut.close();
        Map<String, String> map = new HashMap<String, String>();
        map.put("path", img);
        return map;
    }

    /**
     *
     * @param file 要上传的文件
     * @return
     */
    @RequestMapping("fileUpload")
    public String upload(@RequestParam("fileName") MultipartFile file, Map<String, Object> map){

        // 要上传的目标文件存放路径
        String localPath = "E:/updateLoad/img/";
        // 上传成功或者失败的提示
        String msg = "";

        if (FileUtils.upload(file, localPath, file.getOriginalFilename())){
            // 上传成功，给出页面提示
            msg = "上传成功！";
        }else {
            msg = "上传失败！";

        }

        // 显示图片
        map.put("msg", msg);
        map.put("fileName", file.getOriginalFilename());

        return "forward:/test";
    }

    /**
     * 显示单张图片
     * @return
     */
    @RequestMapping("show")
    public ResponseEntity showPhotos(String fileName){
        String path = "E:/updateLoad/img/";
        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + path + fileName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping("/testpic/{uploadPath}")
    public void testpic(@PathVariable String uploadPath, HttpServletResponse response) throws IOException {
        FileInputStream fis = null;
        File file = new File(path+uploadPath);
        //File file = new File("home/images/test.png"); 服务器目录和本地图片的区别是图片路径
        fis = new FileInputStream(file);
        response.setContentType("image/jpg"); //设置返回的文件类型
        response.setHeader("Access-Control-Allow-Origin", "*");//设置该图片允许跨域访问
        IOUtils.copy(fis, response.getOutputStream());

    }

}
