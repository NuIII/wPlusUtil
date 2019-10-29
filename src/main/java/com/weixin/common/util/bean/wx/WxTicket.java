package com.weixin.common.util.bean.wx;

import java.util.Date;

/**
* @Description:微信签名需要的tickets
* @Author: wangQc
* @Date: 2019/10/29
*/
public class WxTicket {

    private String ticket;
    private Integer expires_in;
    private Date time;

    public String getTicket() {
        return ticket;
    }

    public WxTicket setTicket(String ticket) {
        this.ticket = ticket;
        return this;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public WxTicket setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public WxTicket setTime(Date time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return "WxTickets{" +
                "jsapi_ticket='" + ticket + '\'' +
                ", expires_in=" + expires_in +
                ", time=" + time +
                '}';
    }
}
