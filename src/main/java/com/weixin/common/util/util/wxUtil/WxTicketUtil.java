package com.weixin.common.util.util.wxUtil;

import com.weixin.common.util.bean.wx.WxTicket;
import com.weixin.common.util.util.HttpUtil;
import com.weixin.common.util.util.JsonUtils;

import java.util.Calendar;
import java.util.Date;

/**
* @Description:后台获取ticket
* @Author: wangQc
* @Date: 2019/10/29
*/
public class WxTicketUtil {

    private static final String ticketUrl = "http://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=ACCESS_TOKEN1";
    private static WxTicket wxTicket;

    /**
     * 获取后台操作token
     * @return
     */
    public static String getTicket(){
        if(wxTicket==null||isTicketExpire(wxTicket)){
            try {
                String url = ticketUrl.replace("ACCESS_TOKEN1", WxTokenUtil.getAccessToken());
                String json= HttpUtil.getData(url);
                wxTicket= JsonUtils.string2Obj(json, WxTicket.class);
                wxTicket.setTime(new Date());
                return wxTicket.getTicket();
            } catch (Exception e) {
                return null;
            }
        }else{
            return wxTicket.getTicket();
        }
    }



    /**
     * 判断微信tickets是否过期
     */
    private static boolean isTicketExpire(WxTicket tickets){
        boolean flag=false;
        if(tickets==null) return true;
        if(tickets.getTime()==null) return true;
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.SECOND,-7000);
        if(calendar.getTime().getTime()>tickets.getTime().getTime()) flag=true;
        return flag;
    }

}
