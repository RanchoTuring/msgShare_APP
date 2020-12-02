package com.rancho.msgshare.entity;

import org.litepal.crud.LitePalSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Version extends LitePalSupport {
    String version;
    int userId;
}
