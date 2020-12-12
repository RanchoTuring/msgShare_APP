package com.rancho.msgshare.entity;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

import java.util.Objects;

import lombok.Data;

@Data
public class User extends LitePalSupport {
    @SerializedName("id")
    private int userId;
    private String name;
    private String password;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User u1 = (User) obj;
            return u1.getUserId() == userId && u1.getName().equals(name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name);
    }
}
