package com.eason.blog.req;

public class BloguserQueryReq extends PageReq{

    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String toString() {
        return "BloguserQueryReq{" +
                "loginName='" + loginName + '\'' +
                "} " + super.toString();
    }
}