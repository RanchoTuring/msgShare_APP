package com.rancho.msgshare.common;

public class CommonConstant {

    public static String DEVICE_TYPE_APP = "app";

    public static int NEW_MSG_ID=-1;

    /**
     * 单条msg简介最大字数
     */
    public static int PROFILE_MAX_LENGTH = 30;

    /**
     * 服务器ip
     */
  //  public static String HOST_URL="http://xxxxx/";

    /**
     * 本地测试使用
     */
    public static String HOST_URL="http://10.0.2.2:8080/";

    public static String TEXT_MSG_RES_URL="data/text";

    public static String TEXT_VERSION_RES_URL="data/text/version";

    public static String USER_RES_URL="user";

    /**
     * 后边需要添加用户id
     */
    public static String USER_STATUS_RES_URL="user/status/";

    public static String PARAM_MSG_ID="msgId";
    public static String PARAM_MSG_CONTENT="text";
    public static String PARAM_DEVICE="device";
    public static String PARAM_USERNAME="username";
    public static String PARAM_PASSWORD="password";
}
