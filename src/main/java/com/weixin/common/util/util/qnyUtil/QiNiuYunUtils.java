package com.weixin.common.util.util.qnyUtil;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.weixin.common.util.util.JsonUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class QiNiuYunUtils {

    private static String accessKey = "aQlSc31nvH1lh-q_OkHyIy3ZC1IoYEEH8zCNxX_P";
    private static String secretKey = "3DjcAJHm5q-FFl5qWQzKQcsHSok3NdXeaXhW32bG";
    private static String bucket = "wom-chd";
    private static String DOMAIN = "http://pzgdrh6fj.bkt.clouddn.com/";
    static StringBuffer fileUrl = new StringBuffer(DOMAIN);



    /**
    * @Description: 七牛云保存
    * @Param: [file]
    * @return: java.lang.String
    * @Author: caihaidong
    * @Date: 2019/10/25
    */
    public static String qiniuFile(MultipartFile file) {

        try {
            /**
             * 读取文件
             */
            InputStream inputStream = file.getInputStream();
            byte[] bytes = readStream(inputStream);
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            /**
             *  Configuration 表示带指定Zone对象的配置类
             *  其中Zone 0 表示华东地区
             */
            Configuration cfg = new Configuration(Zone.zone0());
            UploadManager uploadManager = new UploadManager(cfg);
            /**
             * 增加文件后缀名称
             */
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String str = simpleDateFormat.format(new Date());
            String filePath = file.getOriginalFilename();
            String key = str + filePath.substring(filePath.lastIndexOf("/") + 1);
            /**
             * 设置七牛云返回格式以及url
             */
            Auth auth = Auth.create(accessKey, secretKey);
            StringMap putPolicy = new StringMap();
            putPolicy.put("callbackBody", "key=$(key)&hash=$(etag)&bucket=$(bucket)&fsize=$(fsize)");
            putPolicy.put("fileUrl", DOMAIN + "$(key)");

            String upToken = auth.uploadToken(bucket, null, 3600, putPolicy);
            Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
            /**
             * 解析上传成功之后的结果
             */
            DefaultPutRet putRet = JsonUtils.string2Obj(response.bodyString(), DefaultPutRet.class);
            return fileUrl.append(putRet.key).toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (QiniuException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Description: 七牛云保存
     * @Param: [filePath]
     * @return: com.womdata.common.core.model.BaseDataResponse
     * @Author: caihaidong
     * @Date: 2019/10/24
     */
    public static String qiNiuYunFilePath(String filePath) {

        try {

            FileInputStream inputStream = new FileInputStream(filePath);
            byte[] bytes = readStream(inputStream);
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);

            Configuration cfg = new Configuration(Zone.zone0());
            UploadManager uploadManager = new UploadManager(cfg);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String str = simpleDateFormat.format(new Date());
            String key = str + filePath.substring(filePath.lastIndexOf("/") + 1);

            Auth auth = Auth.create(accessKey, secretKey);
            StringMap putPolicy = new StringMap();
            putPolicy.put("callbackBody", "key=$(key)&hash=$(etag)&bucket=$(bucket)&fsize=$(fsize)");
            putPolicy.put("fileUrl", DOMAIN + "$(key)");
            StringBuffer fileUrl = new StringBuffer(DOMAIN);
            String upToken = auth.uploadToken(bucket, null, 3600, putPolicy);
            Response response = uploadManager.put(byteInputStream, key, upToken, null, null);

            DefaultPutRet putRet = JsonUtils.string2Obj(response.bodyString(), DefaultPutRet.class);
            return fileUrl.append(putRet.key).toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (QiniuException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * @Description: 读取上传文件
     * @Param: [inStream]
     * @return: byte[]
     * @Author: caihaidong
     * @Date: 2019/10/16
     */
    public static byte[] readStream(InputStream inStream) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outStream.toByteArray();
    }


}
