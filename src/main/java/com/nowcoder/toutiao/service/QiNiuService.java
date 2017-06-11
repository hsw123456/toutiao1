package com.nowcoder.toutiao.service;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.toutiao.controller.LoginController;
import com.nowcoder.toutiao.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by hsw on 2017/5/26.
 */
@Service
public class QiNiuService {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    Configuration cfg = new Configuration(Zone.zone0());
    //...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);
    //...生成上传凭证，然后准备上传
    String accessKey = "ZyCZEiATqjxL-nPVnMP1nRUV336m3GTTuymMiH5J";
    String secretKey = "QM4r8dPpmSGNSp5Gn1Zugo_tIGkBhd2zkoaXnqqD";
    String bucket = "hsw-website";
    //如果是Windows情况下，格式是 D:\\qiniu\\test.png
   //String localFilePath = "/home/qiniu/test.png";
    //默认不指定key的情况下，以文件内容的hash值作为文件名
    Auth auth = Auth.create(accessKey, secretKey);
    String upToken = auth.uploadToken(bucket);

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if(dotPos < 0){
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos +1).toLowerCase();
            if(!ToutiaoUtil.isFileAllowed(fileExt)){
                return null;
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-","")+"." + fileExt;
            Response res = uploadManager.put(file.getBytes(), fileName, upToken);
            //解析上传成功的结果
            System.out.println(res.bodyString());
            if(res.isJson() && res.isOK()){
                String key = JSONObject.parseObject(res.bodyString()).get("key").toString();
                return ToutiaoUtil.QINIU_DOMAIN_PROFIX + key;
            }else{
                logger.error("七牛上传出错" + res.bodyString());
                return null;
            }
        } catch (QiniuException ex) {
            logger.error("七牛异常" + ex.getMessage());
            return null;
        }
    }

}
