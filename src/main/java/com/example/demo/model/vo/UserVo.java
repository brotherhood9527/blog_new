package com.example.demo.model.vo;/*

 */

import com.example.demo.model.pojo.UserProfile;

import java.util.Date;

public class UserVo {
    private Integer id;

    private Boolean isSuperuser;

    private Boolean isStaff;

    private String username;

    private String email;

    private Date lastLogin;

    private UserProfile userProfile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getSuperuser() {
        return isSuperuser;
    }

    public void setSuperuser(Boolean superuser) {
        isSuperuser = superuser;
    }

    public Boolean getStaff() {
        return isStaff;
    }

    public void setStaff(Boolean staff) {
        isStaff = staff;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getNickNameOrUserName(){
        UserProfile userProfile = this.getUserProfile();
        if(userProfile != null){
            return userProfile.getNickname();
        }else{
            return username;
        }
    }

    public String getNickNameOrEmpty(){
        UserProfile userProfile = this.getUserProfile();
        if(userProfile != null){
            return userProfile.getNickname();
        }else{
            return "";
        }
    }
}
