package com.rancho.msgshare.entity;

import java.util.List;

import lombok.Data;

@Data
public class TextMsgResult {
    int code;
    String msg;
    List<TextMsg> data;
}
