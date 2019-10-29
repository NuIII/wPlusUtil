package com.weixin.common.util.bean.wx;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @Description: 微信的配置
 */
public class WxSetting{

    private static String appId = "wx1bc88e0e1c2172a4";
    private static String appSecret = "ec98b2c87c2533e95b931087133a9e96";
    /**
     * 微信与服务器接入时的验证Token
     */
    private static String token;
    //pc端域名
    private static String domain;
    //移动端域名  消息路径
    private static String mobileDomain;
    
    
//    static {
//        Properties pro = new Properties();
//        try {
//            // 读取配置文件
//            FileInputStream in = new FileInputStream(System.getProperty("asset.app.home")
//                    + "");
//
//            pro.load(new InputStreamReader(in,"utf-8"));
//        } catch (Exception e) {
//        }
//        appId = pro.getProperty("wx_appId");
//        appSecret = pro.getProperty("wx_appSecret");
//        token = pro.getProperty("wx_TOKEN");
//
//    }

    public static String getAppId() {
        return appId;
    }

    public static void setAppId(String appId) {
        WxSetting.appId = appId;
    }

    public static String getAppSecret() {
        return appSecret;
    }

    public static void setAppSecret(String appSecret) {
        WxSetting.appSecret = appSecret;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        WxSetting.token = token;
    }

    public static String getDomain() {
        return domain;
    }

    public static void setDomain(String domain) {
        WxSetting.domain = domain;
    }

    public static String getMobileDomain() {
        return mobileDomain;
    }

    public static void setMobileDomain(String mobileDomain) {
        WxSetting.mobileDomain = mobileDomain;
    }
}
