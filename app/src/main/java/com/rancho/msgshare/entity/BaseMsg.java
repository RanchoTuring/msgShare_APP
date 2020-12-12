package com.rancho.msgshare.entity;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

import java.util.Objects;

import lombok.Data;

@Data
public class BaseMsg extends LitePalSupport implements Comparable<BaseMsg> {
    @SerializedName("id")
    private int msgId;
    /**
     * 后端写入db时生成时间戳，返回前端时序列化为字符串
     */
    private String utime;
    private String source;
    private int userId;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseMsg) {
            BaseMsg u1 = (BaseMsg) obj;
            return u1.getMsgId() == msgId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(msgId);
    }

    /**
     * sorted desc by utime
     * @param o
     * @return
     */
    @Override
    public int compareTo(BaseMsg o) {
        return -utime.compareTo(o.utime);
    }
}
