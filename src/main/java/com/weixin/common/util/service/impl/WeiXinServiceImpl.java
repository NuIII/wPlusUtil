package com.weixin.common.util.service.impl;


import com.weixin.common.util.bean.wx.WeChat;
import com.weixin.common.util.constants.ExceptionConstant;
import com.weixin.common.util.pay.wxPay.MyConfig;
import com.weixin.common.util.pay.wxPay.WXPay;
import com.weixin.common.util.pay.wxPay.WXPayConstants;
import com.weixin.common.util.pay.wxPay.WXPayUtil;
import com.weixin.common.util.service.WeiXinService;
import com.weixin.common.util.util.OrderNoBuilderUtils;
import com.weixin.common.util.util.TextUtil;
import com.weixin.common.util.util.wxUtil.WxUserInfoUtil;
import com.womdata.common.core.exception.WomException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @Title: WeiXinServiceImpl.java
 * @Description: 用户实现类
 */
@Service
public class WeiXinServiceImpl implements WeiXinService {

    /**
     * 微信统一下单
     * @param requestURL
     * @param weChat
     * @return
     */
    @Override
    public Map<String, String> unifiedOrder(StringBuffer requestURL, WeChat weChat) throws Exception {

        Long totalFee = weChat.getTotalFee();


        if(null == totalFee)
            throw new WomException(ExceptionConstant.WOM_EXCEPTION_1001);

        //如果域名不包含sit 是正式环境  采用前端传来的金额
        if(requestURL.toString().contains("sit")
                || requestURL.toString().contains("dev")
                || requestURL.toString().contains("localhost"))
            totalFee = (long)1;

        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "充值");
        data.put("fee_type", "CNY");
        data.put("total_fee", totalFee.toString());
        //TODO 支付成功回调路径
        data.put("notify_url", "/api/weiXin/getPaymentResult");
        data.put("trade_type", weChat.getTradeType());  //JSAPI支付     NATIVE -Native支付    APP -APP支付
        if(TextUtil.notEmpty(weChat.getOpenId())){
            data.put("openid", weChat.getOpenId());
        }
        //TODO 确定一下订单号的格式
        String tradeNo = OrderNoBuilderUtils.getNewId();
        data.put("out_trade_no", tradeNo);


//        System.out.println("==============入参==================");
//        for (String s: data.keySet()) {
//            System.out.println(s+"==================="+data.get(s));
//        }
//        System.out.println("==============入参结束==================");


        Map<String, String> resp = wxpay.unifiedOrder(data);

//        System.out.println("==============回参==================");
//        for (String s: resp.keySet()) {
//            System.out.println(s+"==================="+resp.get(s));
//        }
//        System.out.println("==============回参结束==================");


        resp.put("out_trade_no", tradeNo);
        return resp;
    }

    /**
     * 收到支付结果通知
     * @param request
     * @return
     */
    @Override
    public String getPaymentResult(HttpServletRequest request) throws Exception {
        String notifyData = ""; // 支付结果通知的xml格式数据
            InputStream inStream = request.getInputStream();
            int _buffer_size = 1024;
            if (inStream != null) {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] tempBytes = new byte[_buffer_size];
                int count = -1;
                while ((count = inStream.read(tempBytes, 0, _buffer_size)) != -1) {
                    outStream.write(tempBytes, 0, count);
                }
                tempBytes = null;
                outStream.flush();
                //将流转换成字符串
                notifyData = new String(outStream.toByteArray(), "UTF-8");
            }

            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map

//            System.out.println("==============将流转转换成map========================");
//            for (String s:notifyMap.keySet()) {
//                System.out.println(s+"========="+notifyMap.get(s));
//            }
//            System.out.println("==============将流转转换成map完毕========================");


            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            if (!wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                //如果失败返回错误，微信会再次发送支付信息
                return WXPayConstants.RETURN_PAY_SIGN_FAIL;
            }
//            System.out.println("==============签名正确========================");
//
//            System.out.println("==============开始验证金额是否一致========================");
            // 签名正确
            //判断收到的金额跟商户订单金额是否一致
            Map<String, String> data = new HashMap<>();
            data.put("transaction_id", notifyMap.get("transaction_id"));
            Map<String, String> orderResp = wxpay.orderQuery(data);

//            System.out.println("==============查询订单map========================");
//            for (String s:orderResp.keySet()) {
//                System.out.println(s+"========="+orderResp.get(s));
//            }
//            System.out.println("==============查询订单map完毕========================");

            if(!orderResp.get("total_fee").equals(notifyMap.get("total_fee"))) return WXPayConstants.RETURN_PAY_FEE_FAIL;

//            System.out.println("==============金额验证正确========================");

            // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
            //如果不是退款通知  则新增记录
            if (TextUtil.isEmpty(notifyMap.get("refund_status"))){
                //TODO 这里执行支付成功的业务逻辑
            }
            return  WXPayConstants.RETURN_PAY_SUCCESS;
    }


    /**
     * 获取用户的信息
     * @param weChat
     * @return
     */
    @Override
    public Map<String, String> getWxUserInfo(WeChat weChat) {
        if(TextUtil.isEmpty(weChat.getCode())){
            throw new WomException(ExceptionConstant.WOM_EXCEPTION_1001);
        }
        return WxUserInfoUtil.getUserMap(weChat.getCode());
    }

}
