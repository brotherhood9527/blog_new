package com.example.demo.dao;

import com.example.demo.model.pojo.Blog;
import com.example.demo.model.vo.BlogDateAndBlogCountVo;
import com.example.demo.model.vo.BlogVo;

import java.util.List;

public interface BlogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Blog row);

    int insertSelective(Blog row);

    Blog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Blog row);

    int updateByPrimaryKeyWithBLOBs(Blog row);

    int updateByPrimaryKey(Blog row);

    List<BlogVo> getAllBlogs();

    BlogVo getBlogById(int blogId);

    List<BlogVo> getAllBlogsByBlogTypeId(int blogTypeId);

    List<BlogDateAndBlogCountVo> getBlogDateAndBlogCount();

    List<BlogVo> getAllBlogsByYearAndMonth(int year, int month);

    BlogVo getPreviousBlog(Integer id);

    BlogVo getNextBlog(Integer id);
}