package com.eason.blog.req;

import javax.validation.constraints.NotEmpty;

public class BloguserLoginReq {

    @NotEmpty(message = "Username can not be empty")
    private String loginName;

    @NotEmpty(message = "Password can not be empty")
    private String password;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", loginName=").append(loginName);
        sb.append(", password=").append(password);
        sb.append("]");
        return sb.toString();
    }
}
