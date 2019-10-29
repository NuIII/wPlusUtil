package com.weixin.common.util.util.wxUtil;

import com.weixin.common.util.bean.wx.WxAccessToken;
import com.weixin.common.util.bean.wx.WxSetting;
import com.weixin.common.util.util.HttpUtil;
import com.weixin.common.util.util.JsonUtils;
import org.apache.commons.codec.EncoderException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
* @Description:后台获取access_token
* @Author: wangQc
* @Date: 2019/10/29
*/
public class WxTokenUtil {

    public static final String tokenUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
            + WxSetting.getAppId()+"&secret="+WxSetting.getAppSecret();

    /**
     * 进行微信公众号操作的token
     * 需要appId 和 secret 获取通行令牌
     */
    private static WxAccessToken token;

    /**
     * 获取后台操作token
     * @return
     */
    public static String getAccessToken(){
        if(token==null||isTokenExpire(token)){
            try {
                String json= HttpUtil.getData(tokenUrl);
                token= JsonUtils.string2Obj(json,WxAccessToken.class);
                token.setTime(new Date());
                return token.getAccess_token();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (EncoderException e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return token.getAccess_token();
        }
    }



    /**
     * 判断微信token是否过期
     */
    private static boolean isTokenExpire(WxAccessToken token){
        boolean flag=false;
        if(token==null)
            return true;
        if(token.getTime()==null)
            return true;
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.SECOND,-7000);
        if(calendar.getTime().getTime()>token.getTime().getTime())
            flag=true;
        return flag;
    }


    public static void main(String[]args){
        System.out.println(getAccessToken());
    }
}
