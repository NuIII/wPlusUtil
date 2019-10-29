package com.weixin.common.util.pay.wxPay;



import com.weixin.common.util.bean.wx.WxSetting;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author wangqc
 * @version V2.0
 * @Description:
 * @date 2019/6/17
 * @time 13:16
 */
public class MyConfig extends WXPayConfig{

    private byte[] certData;

    public MyConfig() throws Exception {
        String certPath = System.getProperty("asset.app.home") + "/agent-config/conf/apiclient_cert.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }


    @Override
    String getAppID() {
        return WxSetting.getAppId();
    }

    @Override
    String getMchID() {
        return WXPayConstants.MCH_ID;
    }

    @Override
    String getKey() {
        return WXPayConstants.API_KEY;
    }

    @Override
    InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        // 这个方法需要这样实现, 否则无法正常初始化WXPay
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {

            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;

    }
}
