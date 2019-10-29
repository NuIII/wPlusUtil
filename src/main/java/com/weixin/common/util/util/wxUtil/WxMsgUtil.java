package com.weixin.common.util.util.wxUtil;

import com.weixin.common.util.util.HttpUtil;
import com.weixin.common.util.util.TextUtil;

import java.util.LinkedHashMap;

/**
* @Description:  微信模版消息工具  官方文档：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
* @Author: wangQc
* @Date: 2019/10/29
*/
public class WxMsgUtil {
    /**
     * 文字颜色
     */
    public static final String textColor="#173177";

    /**
     * 顶部颜色
     */
    public static final String topColor="#FF0000";

    /**
     * 模版消息的发送地址
     */
    public static final String  modelMsgUrl="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";


    /**
     * 发送模版消息
     * @param openId
     * @param templateId
     * @param url
     * @param map
     */
    public static void sendModelMsg(String openId, String templateId, String url, LinkedHashMap<String,String> map){
        try {
            String jsonStr=templateJsonFormatter(openId, templateId, url, map);
            String result= HttpUtil.getPostData(modelMsgUrl+ WxTokenUtil.getAccessToken(),jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 模版消息的json生成
     * @param openId
     * @param templateId
     * @param url
     * @param map
     * @return
     */
    private static String templateJsonFormatter(String openId, String templateId, String url, LinkedHashMap<String,String> map){
        String json="";
        json="{\n" +
                "\"touser\":\""+openId+"\",\n" +
                "\"template_id\":\""+templateId+"\",\n" +
                "\"url\":\""+ (TextUtil.notEmpty(url)?url:"")+"\",\n" +
                "\"topcolor\":\""+ topColor+"\",\n" +
                "\"data\":{\n" ;
        String paramStr="";
        for(String key:map.keySet()){
            paramStr+="\""+key+"\": {\n" +
                    "\"value\":\""+map.get(key)+"\",\n" +
                    "\"color\":\""+textColor+"\"\n" +
                    "},";
        }
        paramStr=paramStr.substring(0,paramStr.length()-1);//去除最后一个逗号
        json+= paramStr+"}\n" +
                "}";
        return json;
    }

    public static void main(String []args){
        LinkedHashMap map=new LinkedHashMap();
        map.put("first","尊敬的业主 - XX先生您好");
        map.put("keyword1","水电报修");
        map.put("keyword2","请求已经被受理");
        map.put("remark","感谢您的支持");
        sendModelMsg(
                "oUBPtwQgzuRPpictgxwhBeX-sVNY",
                "gvk_EM8W4hhLP6yl_fujW1qzEADP7C6rfR3GD-QGcAY",
                "消息点击访问的路径",
                map);

    }
}
