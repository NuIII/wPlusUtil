package com.weixin.common.util.controller;

import com.weixin.common.util.bean.wx.WeChat;
import com.weixin.common.util.bean.wx.WxSetting;
import com.weixin.common.util.constants.ExceptionConstant;
import com.weixin.common.util.pay.wxPay.MyConfig;
import com.weixin.common.util.pay.wxPay.WXPay;
import com.weixin.common.util.pay.wxPay.WXPayConstants;
import com.weixin.common.util.service.WeiXinService;
import com.weixin.common.util.util.MD5;
import com.weixin.common.util.util.TextUtil;
import com.weixin.common.util.util.wxUtil.WxOpenIdUtil;
import com.weixin.common.util.util.wxUtil.WxSignUtil;
import com.weixin.common.util.util.wxUtil.WxUserInfoUtil;
import com.womdata.common.core.exception.WomException;
import com.womdata.common.core.model.BaseDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author: wangQC
 * @Date: 2019/10/28
 * @Description:
 */
@RestController
@RequestMapping("/api/weiXin")
public class WxController {

    protected static final Logger logger = LoggerFactory.getLogger(WxController.class);

    @Resource
    private WeiXinService weiXinService;


    /**
    * @Description: 获取权限验证的签名
    * @Param: [request, weChat]
    * @return: com.womdata.common.core.model.BaseDataResponse
    * @Author: wangQc
    * @Date: 2019/10/29
    */
    @PostMapping(value = "/getSignature")
    @ResponseBody
    public BaseDataResponse getSignature(HttpServletRequest request, @RequestBody WeChat weChat) {
        String url = weChat.getUrl();
        Map<String, String> signatureMap = null;
        try {
            signatureMap = WxSignUtil.getSign(request, url);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new BaseDataResponse<>(signatureMap);
    }

    /**
     * 统一下单
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/unifiedOrder")
    @ResponseBody
    public BaseDataResponse unifiedOrder(HttpServletRequest request, @RequestBody WeChat weChat) {
        StringBuffer requestURL = request.getRequestURL();
        Map<String, String> resp = null;

        if (TextUtil.isEmpty(weChat.getCode()))
            throw new WomException(ExceptionConstant.WOM_EXCEPTION_1001);

        try {
            String openId = WxOpenIdUtil.getOpenId(weChat.getCode());
            if(TextUtil.isEmpty(openId))
                throw new WomException(ExceptionConstant.WOM_EXCEPTION_1002);
            resp = weiXinService.unifiedOrder(
                    requestURL,
                    weChat
                            .setTradeType("JSAPI")
                            .setOpenId(openId)
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        //二次签名
        String timeStamp = String.valueOf(Math.round(new Date().getTime() / 1000));
        String appId = WxSetting.getAppId();
        String nonceStr = resp.get("nonce_str");
        String Package = "prepay_id=" + resp.get("prepay_id");
        String signType = WXPayConstants.MD5;

        Map<String, String> doubleSignMap = new HashMap<String, String>();
        doubleSignMap.put("appId", appId);
        doubleSignMap.put("timeStamp", timeStamp);
        doubleSignMap.put("nonceStr", nonceStr);
        doubleSignMap.put("package", Package);
        doubleSignMap.put("signType", signType);

        String paySign = createSign(WXPayConstants.API_KEY, doubleSignMap);
        doubleSignMap.put("paySign", paySign);

        return new BaseDataResponse<>(doubleSignMap);
    }

    /**
     * 生成sign用于请求微信
     * @param key 秘钥
     * @param parameters 参数map
     * @return
     *     
     */
    public static String createSign(String key, Map<String, String> parameters) {

        StringBuffer sb = new StringBuffer();

        if (!(parameters instanceof SortedMap<?, ?>)) {
            parameters = new TreeMap<String, String>(parameters);
        }
        Set<?> es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序），sign参数不参与签名
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        String sign = MD5.getMD5(sb.toString()).toUpperCase();
        return sign;

    }

    /**
    * @Description: 订单查询DEMO
    * @Param: [request]
    * @return: com.womdata.common.core.model.BaseDataResponse
    * @Author: wangQc
    * @Date: 2019/10/29
    */
    @PostMapping(value = "/orderQuery")
    @ResponseBody
    public BaseDataResponse orderQuery(HttpServletRequest request){
        try{
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, String> data = new HashMap<>();
            data.put("out_trade_no", "2016090910595900000012");
            Map<String, String> resp = wxpay.orderQuery(data);

            return  new BaseDataResponse<>(resp);
        }catch (Exception e){
            logger.error("orderQuery====异常：" + e.getMessage(),e);
            return null;
        }
    }


    /**
    * @Description: 退款查询DEMO
    * @Param: [request]
    * @return: com.womdata.common.core.model.BaseDataResponse
    * @Author: wangQc
    * @Date: 2019/10/29
    */
    @PostMapping(value = "/refundQuery")
    @ResponseBody
    public BaseDataResponse refundQuery(HttpServletRequest request){
        try{
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, String> data = new HashMap<>();
            data.put("out_trade_no", "2016090910595900000012");
            Map<String, String> resp = wxpay.refundQuery(data);

            return  new BaseDataResponse<>(resp);
        }catch (Exception e){
            logger.error("refundQuery====异常：" + e.getMessage(),e);
            return null;
        }
    }



    /**
     * 收到支付结果通知时，需要验证签名，可以这样做
     *
     * @return
     */
    @PostMapping(value = "/getPaymentResult")
    @ResponseBody
    public String getPaymentResult(HttpServletRequest request) {
        try {
            String resultXML = weiXinService.getPaymentResult(request);
            return resultXML;
        } catch (Exception e) {
            logger.error("getPaymentResult====异常：" + e.getMessage(), e);
            return WXPayConstants.RETURN_PAY_FAIL;
        }
    }

    /**
    * @Description: 获取用户的信息
    * @Param: [request, weChat]
    * @return: com.womdata.common.core.model.BaseDataResponse
    * @Author: wangQc
    * @Date: 2019/10/29
    */
    @PostMapping(value = "/getWxUserInfo")
    @ResponseBody
    public BaseDataResponse getWxUserInfo(HttpServletRequest request, @RequestBody WeChat weChat) {
        Map<String, String> userInfo = weiXinService.getWxUserInfo(weChat);
        return new BaseDataResponse<>(userInfo);
    }

}
