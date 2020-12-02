package com.rancho.msgshare.entity;

import org.litepal.crud.LitePalSupport;

import lombok.Data;

@Data
public class BaseMsg extends LitePalSupport {
    private int id;
    /**
     * 后端写入db时生成时间戳，返回前端时序列化为字符串
     */
    private String utime;
    private String source;
    private int userId;
}
