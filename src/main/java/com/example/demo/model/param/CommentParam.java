package com.example.demo.model.param;/*

 */

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentParam {
    @NotBlank(message = "评论内容不能为空")
    private String text;

    @NotNull(message = "回复对象不能为空")
    private Integer replyCommentId;

    @NotNull(message = "评论的对象id不能为空")
    private Integer objId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(Integer replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public Integer getObjId() {
        return objId;
    }

    public void setObjId(Integer objId) {
        this.objId = objId;
    }
}
