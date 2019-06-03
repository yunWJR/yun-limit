package com.yun.base.limit;

/**
 * @author: yun
 * @createdOn: 2019-05-31 16:35.
 */

public class YunLimitException extends RuntimeException {
    private int code;

    private String key;

    private RedisLimitPara para;

    public YunLimitException() {
    }

    public YunLimitException(RedisLimitPara para) {
        this.code = -1;
        this.para = para;
    }

    public YunLimitException(String key) {
        this.code = -1;
        this.key = key;
    }

    public static YunLimitException noLimitError() {
        YunLimitException exception = new YunLimitException();
        exception.setCode(-2);

        return exception;
    }

    public RedisLimitPara getPara() {
        return para;
    }

    public void setPara(RedisLimitPara para) {
        this.para = para;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}