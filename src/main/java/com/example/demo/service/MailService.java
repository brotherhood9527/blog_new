package com.example.demo.service;/*

 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送验证码到邮件
     * @param email 收件人邮箱
     */
    public void sendVerificationCodeMail(Integer verificationCode, String email){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        try {
            message.setSubject("绑定邮箱");
            message.setFrom(mailUsername);
            message.setTo(email);
            message.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("verificationCode",verificationCode);
            String text = templateEngine.process("/mail/verificationCode.html",context);
            message.setText(text,true);
        }catch (Exception e){
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }

    /**
     * 发送评论或回复到邮箱
     * @param email 收件人邮箱
     */
    public void sendCommentOrReplyMail(boolean isComment, String commentText, String url, String email){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        try {
            if(isComment){
                message.setSubject("有人评论了你的博客");
            }else{
                message.setSubject("有人回复了你的评论");
            }
            message.setFrom(mailUsername);
            message.setTo(email);
            message.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("text",commentText);
            context.setVariable("url",url);
            String text = templateEngine.process("/mail/comment_reply.html",context);
            message.setText(text,true);
        }catch (Exception e){
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }
}
