package com.example.demo.dao;

import com.example.demo.model.pojo.BlogType;
import com.example.demo.model.vo.BlogTypeVo;

import java.util.List;

public interface BlogTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BlogType row);

    int insertSelective(BlogType row);

    BlogType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BlogType row);

    int updateByPrimaryKey(BlogType row);

    List<BlogTypeVo> getBlogTypeAndBlogCount();
}