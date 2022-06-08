package com.eason.blog.exception;

public enum BusinessExceptionCode {

    USER_LOGIN_NAME_EXIST("UserName already exists"),
    LOGIN_USER_ERROR("Username does not exist or Password is incorrect"),
    VOTE_REPEAT("You already voted this blog!"),
    ;

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
