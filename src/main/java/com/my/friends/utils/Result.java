package com.my.friends.utils;

/**
 * @Atuhor: qin
 * @Create: 2022-01-20-21-11
 * @Time: 21:11
 * @Description:
 */

//        Map<String, Object> dataMap = new HashMap<String, Object>();
//        dataMap.put("data", data);
//        dataMap.put("userid", userid);

// result = Result.success(dataMap);// 成功，并返回数据和Code和message
// result = Result.success();// 成功，不返回数据，只返回Code和message
// result = Result.error(CodeMsg.SERVER_EXCEPTION);// 失败返回错误信息
// result = Result.error(CodeMsg.SERVER_EXCEPTION,e.toString());// 失败返回错误+扩展错误信息
public class Result<T> {
    private String message;
    private int Code;
    private T data;
    private Result(T data) {
        this.Code = 0;
        this.message = "成功";
        this.data = data;
    }
    private Result(CodeMsg cm) {
        if(cm == null){
            return;
        }
        this.Code = cm.getcode();
        this.message = cm.getMessage();
    }
    /**
     * 成功时候的调用
     * @return
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }
    /**
     * 成功，不需要传入参数
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> success(){
        return (Result<T>) success("");
    }
    /**
     * 失败时候的调用
     * @return
     */
    public static <T> Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
    }
    /**
     * 失败时候的调用,扩展消息参数
     * @param cm
     * @param msg
     * @return
     */
    public static <T> Result<T> error(CodeMsg cm,String msg){
        if(cm.getMessage().contains("：")){
            cm.setMessage(cm.getMessage());
        }else{
            cm.setMessage(cm.getMessage()+"："+msg);
        }
        return new Result<T>(cm);
    }
    public T getData() {
        return data;
    }
    public String getMessage() {
        return message;
    }
    public int getCode() {
        return Code;
    }
}
