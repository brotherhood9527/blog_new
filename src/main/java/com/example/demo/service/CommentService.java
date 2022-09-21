package com.example.demo.service;/*

 */

import com.example.demo.dao.CommentMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.model.pojo.Comment;
import com.example.demo.model.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    public List<CommentVo> getAllCommentsByContentTypeIdAndObjId(Integer contentTypeId, Integer objId){
        return commentMapper.getAllCommentsByContentTypeIdAndObjId(contentTypeId, objId);
    }

    public Map<String, Object> updateComment(Integer objId, Integer contentTypeId, Integer userId, Integer replyCommentId, String text){
        Map<String,Object> updateComment = new HashMap();
        Comment comment = new Comment();
        comment.setObjectId(objId);
        comment.setCommentTime(new Date(System.currentTimeMillis()));
        comment.setContentTypeId(contentTypeId);
        comment.setUserId(userId);
        if(replyCommentId != 0){
            //如果是回复，而不是评论
            comment.setParentId(replyCommentId);
            Comment parent = commentMapper.selectByPrimaryKey(replyCommentId);
            comment.setReplyToId(parent.getUserId());
            Integer rootId = parent.getRootId();
            if(rootId != null){
                comment.setRootId(parent.getRootId());
                updateComment.put("root_pk", parent.getRootId());
            }else{
                comment.setRootId(parent.getId());
                updateComment.put("root_pk", parent.getId());
            }
            String nickNameOrUserName = userMapper.selectUserWithProfileByUserId(parent.getUserId()).getNickNameOrUserName();
            updateComment.put("reply_to",nickNameOrUserName);
        }
        comment.setText(text);
        commentMapper.insertSelective(comment);
        updateComment.put("pk",comment.getId());
        updateComment.put("comment_time", comment.getCommentTime().getTime());
        return updateComment;
    }

}
