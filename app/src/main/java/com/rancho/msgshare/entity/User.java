package com.rancho.msgshare.entity;

import org.litepal.crud.LitePalSupport;

import lombok.Data;

@Data
public class User extends LitePalSupport {
    private int id;
    private String name;
    private String password;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User u1 = (User) obj;
            return u1.getId() == id && u1.getName().equals(name);
        }
        return false;
    }
}
