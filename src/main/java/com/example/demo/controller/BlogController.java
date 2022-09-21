package com.example.demo.controller;/*

 */

import com.example.demo.model.vo.BlogVo;
import com.example.demo.model.vo.CommentVo;
import com.example.demo.model.vo.UserVo;
import com.example.demo.service.*;
import com.example.demo.utils.ReadStatistics;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {
    private final static Integer blogModelId = 8;

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogTypeService blogTypeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private PageHelper pageHelper;

    @Autowired
    private UserService userService;

    public void getCommmonListData(int pageNum, Model model, List<BlogVo> allBlogs){
        //为每一篇博客添加阅读数 likeNum commentNUm
        for(BlogVo blogVo: allBlogs){
            Integer id = blogVo.getId();
            blogVo.setReadNum(ReadStatistics.getReadNum(blogModelId, id));
            blogVo.setLikeNum(likeService.getLikeNum(blogModelId, id));
            blogVo.setCommentNum(commentService.getAllCommentsByContentTypeIdAndObjId(blogModelId, id).size());
        }
        PageInfo<BlogVo> pageInfo = new PageInfo<>(allBlogs);
        int pages = pageInfo.getPages();
        //生成前后为2页的页码范围，并加入省略标记和首页和尾页
        List<String> pageRange = new ArrayList<String>();
        for(int i = Math.max(pageNum -2,1);i <= Math.min(pageNum+2,pages); i++){
            pageRange.add(String.valueOf(i));
        }
        //加入省略标记
        if(Integer.parseInt(pageRange.get(0)) - 1 >= 2){
            pageRange.add(0,"...");
        }
        if(pages - Integer.parseInt(pageRange.get(pageRange.size() - 1)) >= 2){
            pageRange.add("...");
        }
        //加入首页和尾页
        if(!"1".equals((String)pageRange.get(0))){
            pageRange.add(0,"1");
        }
        if(!(pageRange.get(pageRange.size() - 1)).equals(String.valueOf(pages))){
            pageRange.add(String.valueOf(pages));
        }
        model.addAttribute("blogTypes", blogTypeService.getBlogTypeAndBlogCount());
        model.addAttribute("blogDateAndBlogCount", blogService.getBlogDateAndBlogCount());
        model.addAttribute("allBlogs", pageInfo.getList());
        model.addAttribute("BlogCount", pageInfo.getTotal());
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pages", pages);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pageRange", pageRange);
    }

    @RequestMapping("")
    public String blogList(@RequestParam(value = "pageNum",defaultValue = "1",required = false) int pageNum, Model model){
        //5表示每一页的数量
        PageHelper.startPage(pageNum,5);
        List<BlogVo> allBlogs = blogService.getAllBlogs();
        getCommmonListData(pageNum,model,allBlogs);
        return "/blog/blog_list";
    }

    @RequestMapping("/type/{blog_type_pk}")
    public String blogType(@RequestParam(value = "pageNum",defaultValue = "1",required = false) int pageNum, @PathVariable("blog_type_pk") int blogTypePk, Model model){
        //5表示每一页的数量
        PageHelper.startPage(pageNum,5);
        List<BlogVo> allBlogs = blogService.getAllBlogsByBlogTypeId(blogTypePk);
        getCommmonListData(pageNum,model,allBlogs);
        model.addAttribute("typeName",blogTypeService.selectByPrimaryKey(blogTypePk).getTypeName());
        return "/blog/blog_with_type";
    }

    @RequestMapping("/date/{year}/{month}")
    public String blogsDate(@RequestParam(value = "pageNum",defaultValue = "1",required = false) int pageNum, @PathVariable("year") int year, @PathVariable("month") int month, Model model){
        //5表示每一页的数量
        PageHelper.startPage(pageNum,5);
        List<BlogVo> allBlogsByYearAndMonth = blogService.getAllBlogsByYearAndMonth(year, month);
        getCommmonListData(pageNum,model,allBlogsByYearAndMonth);
        model.addAttribute("blogsWithDate",year + "年" + month + "月");
        return "/blog/blogs_with_date";
    }

    @RequestMapping("/{blogPk}")
    public String blogDetail(@PathVariable("blogPk") int blogPk, HttpServletRequest request, HttpServletResponse response, Model model){
        BlogVo blogById = blogService.getBlogById(blogPk);
        model.addAttribute("blog",blogById);
        String cookieKey = ReadStatistics.readStatisticsOnceRead(request, blogModelId, blogPk);
        blogById.setReadNum(ReadStatistics.getReadNum(blogModelId, blogPk));
        Cookie cookie = new Cookie(cookieKey,"true");
        cookie.setMaxAge(60);
        response.addCookie(cookie);
        HttpSession session = request.getSession();
        UserVo userVo = (UserVo)session.getAttribute("user");
        String blogLikeStatus = "";
        if(userVo != null){
            blogLikeStatus = likeService.getLikeStatus(blogModelId, blogPk, userVo.getId());
        }
        List<CommentVo> allCommentsByContentTypeIdAndObjId = commentService.getAllCommentsByContentTypeIdAndObjId(CommentFormController.commentModelId, blogPk);
        for(CommentVo commentVo : allCommentsByContentTypeIdAndObjId){
            Integer contentTypeId = commentVo.getContentTypeId();
            Integer objectId = commentVo.getId();
            String likeStatus = "";
            if(userVo != null){
                likeStatus = likeService.getLikeStatus(contentTypeId, objectId, userVo.getId());
            }
            commentVo.setLikeStatus(likeStatus);
            commentVo.setLikeCount(likeService.getLikeNum(contentTypeId, objectId));
            List<CommentVo> children = commentVo.getChildren();
            if(!children.isEmpty()){
                for(CommentVo child : children){
                    Integer childContentTypeId = child.getContentTypeId();
                    Integer childObjectId = child.getId();
                    String childLikeStatus = "";
                    if(userVo != null){
                        childLikeStatus = likeService.getLikeStatus(childContentTypeId, childObjectId, userVo.getId());
                    }
                    child.setLikeStatus(childLikeStatus);
                    child.setLikeCount(likeService.getLikeNum(childContentTypeId, childObjectId));
                }
            }
        }
        model.addAttribute("previousBlog", blogService.getPreviousBlog(blogPk));
        model.addAttribute("nextBlog", blogService.getNextBlog(blogPk));
        model.addAttribute("allComments", allCommentsByContentTypeIdAndObjId);
        model.addAttribute("blogCommentCount",allCommentsByContentTypeIdAndObjId.size());
        model.addAttribute("blogLikeStatus", blogLikeStatus);
        model.addAttribute("blogLikeCount", likeService.getLikeNum(blogModelId, blogPk));
        return "/blog/blog_detail";
    }
}
