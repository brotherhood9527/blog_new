package com.example.demo.dao;

import com.example.demo.model.pojo.Comment;
import com.example.demo.model.vo.CommentVo;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment row);

    int insertSelective(Comment row);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment row);

    int updateByPrimaryKeyWithBLOBs(Comment row);

    int updateByPrimaryKey(Comment row);

    List<CommentVo> getAllCommentsByContentTypeIdAndObjId(Integer contentTypeId, Integer objId);

    List<CommentVo> getChildrenByRootId(Integer rootId);
}