package com.rancho.msgshare.entity;

import lombok.Data;

@Data
public class CommonResult {
    int code;
    String msg;
    Object data;
}
