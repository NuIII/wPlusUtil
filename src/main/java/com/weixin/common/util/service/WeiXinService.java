package com.weixin.common.util.service;



import com.weixin.common.util.bean.wx.WeChat;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
* @Description:微信交互service
* @Author: wangQc
* @Date: 2019/10/28
*/
public interface WeiXinService{

    /**
     * 微信统一下单
     * @param requestURL
     * @param weChat
     * @return
     */
    Map<String,String> unifiedOrder(StringBuffer requestURL, WeChat weChat) throws Exception;

    /**
     * 收到支付结果通知
     * @param request
     * @return
     */
    String getPaymentResult(HttpServletRequest request) throws Exception;

    /**
     * 获取用户的信息
     * @param weChat
     * @return
     */
    Map<String,String> getWxUserInfo(WeChat weChat);
}