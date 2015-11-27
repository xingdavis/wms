package com.xs.wms.pojo;

public class User {
    private Integer id;

    private String uname;

    private String pwd;

    private Integer age;
    
    private Integer roleid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname == null ? null : uname.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleId(Integer roleid) {
        this.roleid = roleid;
    }
}