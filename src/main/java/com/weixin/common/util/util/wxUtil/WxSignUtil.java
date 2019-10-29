package com.weixin.common.util.util.wxUtil;


import com.weixin.common.util.bean.wx.WxSetting;
import com.weixin.common.util.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
* @Description: 微信前端掉微信的js-sdk需要微信的签名认证
* @Author: wangQc
* @Date: 2019/10/29
*/
public class WxSignUtil {


    /**
    * @Description: 获取签名数据
    * @Param: [request, url]
    * @return: java.util.Map<java.lang.String,java.lang.String>
    * @Author: wangQc
    * @Date: 2019/10/29
    */
    public static Map<String, String> getSign(HttpServletRequest request, String url) throws Exception {
        String jsApiTicket = WxTicketUtil.getTicket();

        Map<String, String> ret = sign(jsApiTicket, url);
        request.setAttribute("url", ret.get("url"));
        request.setAttribute("jsapi_ticket", ret.get("jsapi_ticket"));
        request.setAttribute("nonceStr", ret.get("nonceStr"));
        request.setAttribute("timestamp", ret.get("timestamp"));
        request.setAttribute("signature", ret.get("signature"));
        request.setAttribute("appId", WxSetting.getAppId());
        ret.put("appId",WxSetting.getAppId());
        return ret;
    };


    /**
     * @Description: 微信签名方法
     * @Param: [jsapi_ticket, url]
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @Author: wangQc
     * @Date: 2019/10/29
     */
    private static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = UUID.randomUUID().toString().replaceAll("-", "")
                .substring(0, 16).toLowerCase();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String string1;
        String signature = "";
        // 注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
                + "&timestamp=" + timestamp + "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}
