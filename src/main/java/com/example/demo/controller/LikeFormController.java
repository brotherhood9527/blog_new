package com.example.demo.controller;/*

 */

import com.example.demo.annotation.LoginAnnotation;
import com.example.demo.model.Bo.LikeCountBo;
import com.example.demo.model.Bo.LikeRecordBo;
import com.example.demo.model.pojo.LikeCount;
import com.example.demo.model.pojo.LikeRecord;
import com.example.demo.model.vo.UserVo;
import com.example.demo.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/like")
public class LikeFormController {
    @Autowired
    private LikeService likeService;

    public Map<String,Object> errorResponse(String code, String message){
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status", false);
        errorResponse.put("code", code);
        errorResponse.put("message", message);
        return errorResponse;
    }

    public Map<String,Object> successResponse(Integer likedNum){
        Map<String,Object> successResponse = new HashMap<>();
        successResponse.put("status", true);
        successResponse.put("liked_num", likedNum);
        return successResponse;
    }

    @PostMapping("/likeChange")
    @LoginAnnotation(name = "点赞", intoDb = true)
    public Object likeChange(String contentTypeIdString, String objIdString, boolean isLike, HttpServletRequest request){
        Map<String,Object> likeChange = new HashMap<>();
        HttpSession session = request.getSession();
        Integer contentTypeId = Integer.valueOf(contentTypeIdString);
        Integer objId = Integer.valueOf(objIdString);
        UserVo user = (UserVo)session.getAttribute("user");
        if(user == null){
            return errorResponse("400","you were not login");
        }
        Integer userId = user.getId();
        if(isLike == true){
            //需要点赞
            LikeRecordBo likeRecordBo = likeService.getORCreateLikeRecordBo(contentTypeId, objId, userId);
            if(likeRecordBo.getCreated()){
                //当前用户未点赞过该contentType、obj，前端传递正常数据
                LikeCountBo orCreateLikeCountBo = likeService.getORCreateLikeCountBo(contentTypeId, objId);
                LikeCount likeCount = orCreateLikeCountBo.getLikeCount();
                likeCount.setLikedNum(likeCount.getLikedNum()+1);
                likeService.updateByPrimaryKeySelectiveLikeCount(likeCount);
                return successResponse(likeCount.getLikedNum());
            }else{
                //当前用户已经点赞过该contentType、obj，前端传递非法数据
                return errorResponse("402","you were liked");
            }
        }else{
            //取消点赞
            List<LikeRecord> likeRecords = likeService.selectBycontentTypeIdAndobjIDAnduserIdLikeRecord(contentTypeId, objId, userId);
            if(!likeRecords.isEmpty()){
                //取消点赞
                LikeRecord likeRecord = likeRecords.get(0);
                likeService.deleteByPrimaryKey(likeRecord.getId());
                LikeCountBo orCreateLikeCountBo = likeService.getORCreateLikeCountBo(contentTypeId, objId);
                if(!orCreateLikeCountBo.getCreated()){
                    LikeCount likeCount = orCreateLikeCountBo.getLikeCount();
                    likeCount.setLikedNum(likeCount.getLikedNum()-1);
                    likeService.updateByPrimaryKeySelectiveLikeCount(likeCount);
                    return successResponse(likeCount.getLikedNum());
                }else{
                    return errorResponse("404","data error");
                }

            }else{
                //当前用户没有点赞过contentType、obj，前端传递非法数据
                return errorResponse("403","you were not liked");
            }
        }
    }
}
