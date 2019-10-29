package com.weixin.common.util.bean.wx;

import java.util.Date;

/**
 * 这是微信后台进行相关操作的token，而非获取用户信息的token
 */
public class WxAccessToken {
    private String access_token;
    private Integer expires_in;
    private Date time;



    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }
    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("WxAccessToken{");
        sb.append("access_token='").append(access_token).append('\'');
        sb.append(", expires_in=").append(expires_in);
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }
}
