package com.xonestep.one.jcsd.sonaroneoauth;

import com.google.gson.Gson;

/**
 * created by jinxingjia on 18-10-27 下午7:39
 **/
public class GsonUser {
    private String username;
    private String name;
    private String email;

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public static GsonUser parse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, GsonUser.class);
    }
}
