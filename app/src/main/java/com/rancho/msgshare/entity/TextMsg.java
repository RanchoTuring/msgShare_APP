package com.rancho.msgshare.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TextMsg extends BaseMsg{
    private String content;
}
