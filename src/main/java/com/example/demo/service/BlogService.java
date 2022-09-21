package com.example.demo.service;/*

 */

import com.example.demo.dao.BlogMapper;
import com.example.demo.model.pojo.Blog;
import com.example.demo.model.vo.BlogDateAndBlogCountVo;
import com.example.demo.model.vo.BlogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private  BlogMapper blogMapper;

    public List<BlogVo> getAllBlogs() {
        return blogMapper.getAllBlogs();
    }

    public BlogVo getBlogById(int blogId){
        return blogMapper.getBlogById(blogId);
    }

    public List<BlogVo> getAllBlogsByBlogTypeId(int blogTypeId){
        return blogMapper.getAllBlogsByBlogTypeId(blogTypeId);
    }

    public List<BlogDateAndBlogCountVo> getBlogDateAndBlogCount(){
        return blogMapper.getBlogDateAndBlogCount();
    }

    public List<BlogVo> getAllBlogsByYearAndMonth(int year, int month){
        return blogMapper.getAllBlogsByYearAndMonth(year,month);
    }

    public Blog selectByPrimaryKey(Integer id){
        return blogMapper.selectByPrimaryKey(id);
    }

    public BlogVo getPreviousBlog(Integer id){
        return blogMapper.getPreviousBlog(id);
    }

    public BlogVo getNextBlog(Integer id){
        return blogMapper.getNextBlog(id);
    }

    public int insertSelective(Blog row){ return blogMapper.insertSelective(row); }
}
