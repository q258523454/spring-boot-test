package com.fastdfs.controller;

import com.alibaba.fastjson.JSONObject;
import com.fastdfs.global.FastDFSConfig;
import com.fastdfs.service.impl.FastDFSClientImpl;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UrlPathHelper;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * Created By
 *
 * @date :   2018-09-20
 */

@RestController
public class FastDfsController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FastDFSConfig fastDFSConfig;

    private UrlPathHelper urlPathHelper = new UrlPathHelper();


    /***
     *
     * @param file
     * @param width  0表示使用原始图片默认宽度, width!=0 height=0 的时候, 按照 width 比例缩放
     * @param height 0表示使用原始图片默认高度, width==0 height!=0 的时候, 按照 height 比例缩放
     *               height!=0 width!=0按设定值缩放
     * @return 返回文件路径 {"storageFilePath":"xxxx/xxxx"}
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(MultipartFile file, int width, int height) {
        JSONObject jsonObject = new JSONObject();
        String storageFilePath = "";
        if (!file.isEmpty()) {
            try {
                // 获取原始片的宽度和高度
                BufferedImage image = ImageIO.read(file.getInputStream());
                Integer widthOrg = image.getWidth();
                Integer heightOrg = image.getHeight();
                String fileExtName = getSuffix(file.getOriginalFilename());
                FastDFSClientImpl client = new FastDFSClientImpl(fastDFSConfig);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                if (height < 0 || width < 0) {
                    throw new IllegalArgumentException("图片长宽参数必须有值.");
                }
                if (height == 0 && width == 0) {
                    // 无需做压缩转换, 直接file
                    storageFilePath = client.uploadFile(file.getBytes(), fileExtName);
                } else if (height > 0 && width > 0) {
                    Thumbnails.of(file.getInputStream()).size((int) width, (int) height).keepAspectRatio(false).outputQuality(1.0f).toOutputStream(bos);
                    storageFilePath = client.uploadFile(bos.toByteArray(), fileExtName);
                } else if (width == 0) {
                    double ratio = (double) height / heightOrg;
                    double heightFinal = height;
                    double widthFinal = (double) widthOrg * ratio;
                    Thumbnails.of(file.getInputStream()).size((int) widthFinal, (int) heightFinal).keepAspectRatio(false).outputQuality(1.0f).toOutputStream(bos);
                    storageFilePath = client.uploadFile(bos.toByteArray(), fileExtName);
                } else if (height == 0) {
                    double ratio = (double) width / widthOrg;
                    double heightFinal = (double) heightOrg * ratio;
                    double widthFinal = width;
                    Thumbnails.of(file.getInputStream()).size((int) widthFinal, (int) heightFinal).keepAspectRatio(false).outputQuality(1.0f).toOutputStream(bos);
                    storageFilePath = client.uploadFile(bos.toByteArray(), fileExtName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //("文件上传失败");
            }
        }
        System.out.println("文件存储地址:" + storageFilePath);
        return "文件上传成功";
    }


    /**
     * 查看(查),返回字节
     * http://IP:PORT/group1/M00/00/01/oYYBAFb6cnKAB1s9AADzeNK1qsI697.jpg
     */
    @GetMapping(value = "/getFileInfoByByte/group1/**", produces = "application/json; charset=UTF-8")
    public byte[] getFileInfoByByte(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=31536000");
        String reqPath = urlPathHelper.getLookupPathForRequest(request);
        byte[] imgBytes = new byte[]{};
        try {
            // storageFilePath like 'group1/M00/00/01/xx.jpg'
            String storageFilePath = reqPath.substring(reqPath.indexOf("/getFileInfoByByte/") + "/getFileInfoByByte/".length());
            FastDFSClientImpl fileClient = new FastDFSClientImpl(fastDFSConfig);
            imgBytes = fileClient.downloadFile(storageFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgBytes;
    }



    /**
     * 获取文件后缀
     *
     * @param originalFilename
     * @return
     */
    private String getSuffix(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
    }

}
