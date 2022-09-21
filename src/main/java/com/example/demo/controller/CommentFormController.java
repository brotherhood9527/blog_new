package com.example.demo.controller;/*

 */

import com.example.demo.model.param.CommentParam;
import com.example.demo.model.vo.UserVo;
import com.example.demo.service.CommentService;
import com.example.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentFormController {
    public final static Integer commentModelId = 12;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MailService mailService;

    @RequestMapping("/updateComment")
    public Object updateComment(@RequestBody @Valid CommentParam commentParam, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            //异常处理
        }
        Map<String,Object> updateComment = new HashMap();
        HttpSession session = request.getSession();
        UserVo user = (UserVo)session.getAttribute("user");
        updateComment = commentService.updateComment(commentParam.getObjId(), commentModelId, user.getId(), commentParam.getReplyCommentId(), commentParam.getText());
        //发送邮件
        updateComment.put("status", true);
        updateComment.put("username", user.getNickNameOrUserName());
        updateComment.put("text", commentParam.getText());
        updateComment.put("content_type", commentModelId);
        return updateComment;
    }


}
