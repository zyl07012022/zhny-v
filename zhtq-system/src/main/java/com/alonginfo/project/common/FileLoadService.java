package com.alonginfo.project.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * @Author ljg
 * @Date 2021/7/28 12:29
 */
@Service("fileLoadService")
public class FileLoadService {
    protected final Log logger = LogFactory.getLog(getClass());

    public void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName, String filePath)
    {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream out = null;
        InputStream in = null;

        File downFiles = new File(filePath);
        if (!downFiles.exists()) {
            return;
        }
        try
        {
            in = new FileInputStream(downFiles);
            bis = new BufferedInputStream(in);
            out = response.getOutputStream();
            bos = new BufferedOutputStream(out);

            setFileDownloadHeader(request, response, fileName);
            int byteRead = 0;
            byte[] buffer = new byte[8192];
            while ((byteRead = bis.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, byteRead);
            }
            bos.flush();
            in.close();
            bis.close();
            out.close();
            bos.close();
        }
        catch (Exception localException) {}
    }
    protected void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName)
    {
        try
        {
            String encodedfileName = null;
            String agent = request.getHeader("USER-AGENT");
            if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
                encodedfileName = URLEncoder.encode(fileName, "UTF-8");
            } else if ((agent != null) && (-1 != agent.indexOf("Mozilla"))) {
                encodedfileName = new String(fileName.getBytes("UTF-8"),
                        "iso-8859-1");
            } else {
                encodedfileName = URLEncoder.encode(fileName, "UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment; filename=\"" +
                    encodedfileName + "\"");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
}
