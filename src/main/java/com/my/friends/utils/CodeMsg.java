package com.my.friends.utils;

/**
 * @Atuhor: qin
 * @Create: 2022-01-20-21-11
 * @Time: 21:11
 * @Description:
 */
public class CodeMsg {
    private int code;
    private String message;
    // 按照模块定义CodeMsg
    // 通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_EXCEPTION = new CodeMsg(500100,"服务端异常");
    public static CodeMsg PARAMETER_ISNULL = new CodeMsg(500101,"输入参数为空");
    // 业务异常
    public static CodeMsg USER_NOT_EXSIST = new CodeMsg(500102,"登录失败");
    public static CodeMsg ONLINE_USER_OVER = new CodeMsg(500103,"在线用户数超出允许登录的最大用户限制。");
    public static CodeMsg SESSION_NOT_EXSIST =  new CodeMsg(500104,"登录失效");
    public static CodeMsg NOT_FIND_DATA = new CodeMsg(500105,"查找不到对应数据");
    public static CodeMsg OP_FAILED = new CodeMsg(500105,"操作失败");

    private CodeMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getcode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message ="";
        this.message = message;
    }
}