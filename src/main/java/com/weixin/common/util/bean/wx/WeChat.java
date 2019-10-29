package com.weixin.common.util.bean.wx;

/**
* @Description:微信验证实体类
* @Author: wangQc
* @Date: 2019/10/29
*/
public class WeChat {
    // 包含token的字符串
    private String signature;
    // 时间戳
    private String timestamp;
    // 随机数
    private String nonce;
    // 随机字符串
    private String echostr;

    private String url;

    private String code;

    //支付金额
    private Long totalFee;

    //支付方式
    // JSAPI -JSAPI支付
    // NATIVE -Native支付
    // APP -APP支付
    private String tradeType;

    //用户的微信唯一识别号
    private String openId;

    //微信下单的订单号
    private String outTradeNo;

    public String getSignature() {
        return signature;
    }

    public WeChat setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public WeChat setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getNonce() {
        return nonce;
    }

    public WeChat setNonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    public String getEchostr() {
        return echostr;
    }

    public WeChat setEchostr(String echostr) {
        this.echostr = echostr;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public WeChat setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getCode() {
        return code;
    }

    public WeChat setCode(String code) {
        this.code = code;
        return this;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public WeChat setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
        return this;
    }


    public String getTradeType() {
        return tradeType;
    }

    public WeChat setTradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public WeChat setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public WeChat setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }
}
