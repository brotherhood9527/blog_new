package com.example.demo.model.pojo;

import java.util.Date;

public class LikeRecord {
    private Integer id;

    private Integer objectId;

    private Date likedTime;

    private Integer contentTypeId;

    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Date getLikedTime() {
        return likedTime;
    }

    public void setLikedTime(Date likedTime) {
        this.likedTime = likedTime;
    }

    public Integer getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(Integer contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}