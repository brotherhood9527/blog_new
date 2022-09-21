package com.example.demo.model.Bo;/*

 */

import com.example.demo.model.pojo.LikeCount;

public class LikeCountBo {
    private LikeCount likeCount;

    private Boolean created;

    public LikeCount getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(LikeCount likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getCreated() {
        return created;
    }

    public void setCreated(Boolean created) {
        this.created = created;
    }
}
