package com.weixin.common.util.util.wxUtil;

import com.weixin.common.util.bean.wx.WxSetting;
import com.weixin.common.util.util.HttpUtil;
import com.weixin.common.util.util.JsonUtils;
import com.weixin.common.util.util.TextUtil;

import java.util.Map;

/**
* @Description: 微信获取用户OpenId的工具类
* @Author: wangQc
* @Date: 2019/10/29
*/
public class WxOpenIdUtil {
    private static final String openIdUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ WxSetting.getAppId()+"&secret="+WxSetting.getAppSecret()+"&code=CODE1&grant_type=authorization_code";

    public static String getOpenId(String code){
        String openId = null;
        try {
            if (TextUtil.notEmpty(code)) {
                String url = openIdUrl.replace("CODE1", code);
                String result = HttpUtil.getData(url);
                openId = result
                        .split("openid")[1]
                        .split("scope")[0]
                        .replaceAll("\"", "")
                        .replaceAll(",", "")
                        .replaceAll(" ", "")
                        .replaceAll(":", "");
            }
        }catch (Exception e){
            return openId;
        }
        return openId;
    }

    /**
    * @Description: 获取调用OpenId链接后的所有返回值 Map的形式
    * @Param: [code]
    * @return: Map
    * @Author: wangQc
    * @Date: 2019/10/29
    */
    public static Map<String,String> getOpenIdAllResult(String code){
        Map<String,String> resultMap = null;
        try {
            if (TextUtil.notEmpty(code)) {
                String url = openIdUrl.replace("CODE1", code);
                //获取到json串
                String result = HttpUtil.getData(url);
                //转成Map
                resultMap = JsonUtils.string2Obj(result, Map.class, String.class, String.class);
            }
        }catch (Exception e){
            return resultMap;
        }
        return resultMap;
    }
}
