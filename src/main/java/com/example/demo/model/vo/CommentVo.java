package com.example.demo.model.vo;/*

 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentVo {
    private Integer id;

    private Integer objectId;

    private Date commentTime;

    private Integer contentTypeId;

    private Integer parentId;

    private String text;

    private UserVo userVo;

    private UserVo replyToUserVo;

    private Integer likeCount;

    private String likeStatus;

    private List<CommentVo> children;

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

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public Integer getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(Integer contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public List<CommentVo> getChildren() {
        return children;
    }

    public void setChildren(List<CommentVo> children) {
        this.children = children;
    }

    public void add(CommentVo commentVo){
        if(children == null){
            children = new ArrayList<CommentVo>();
        }
        children.add(commentVo);
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    public UserVo getReplyToUserVo() {
        return replyToUserVo;
    }

    public void setReplyToUserVo(UserVo replyToUserVo) {
        this.replyToUserVo = replyToUserVo;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }
}
