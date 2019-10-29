package com.weixin.common.util.util.wxUtil;


import com.weixin.common.util.util.HttpUtil;
import com.weixin.common.util.util.JsonUtils;

import java.util.*;


public class WxUserInfoUtil {

    /**
     * 拉取用户信息 url
     */
    private static final String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * @Description:
     * @Param: [accessToken, openId]
     * @return: java.util.Map<java.lang.String   ,   java.lang.String>
     * @Author: wangQc
     * @Date: 2019/10/29
     */
    public static Map<String, String> getUserMap(String code) {
        Map<String, String> userInfoMap = null;
        try {
            //先获取OpenId的Map
            Map<String, String> openIdMap = WxOpenIdUtil.getOpenIdAllResult(code);
            String url = userInfoUrl
                    .replace("ACCESS_TOKEN", openIdMap.get("access_token"))
                    .replace("OPENID", openIdMap.get("openid"));
            //调用获取用户信息接口
            String result = HttpUtil.getData(url);
            userInfoMap = JsonUtils.string2Obj(result, Map.class, String.class, Object.class);
        } catch (Exception e) {
            return userInfoMap;
        }
        return userInfoMap;
    }


}

