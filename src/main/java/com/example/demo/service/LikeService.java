package com.example.demo.service;/*

 */

import com.example.demo.dao.LikeCountMapper;
import com.example.demo.dao.LikeRecordMapper;
import com.example.demo.model.Bo.LikeCountBo;
import com.example.demo.model.Bo.LikeRecordBo;
import com.example.demo.model.pojo.LikeCount;
import com.example.demo.model.pojo.LikeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static javax.swing.UIManager.get;

@Service
public class LikeService {
    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Autowired
    private LikeCountMapper likeCountMapper;

    public String getLikeStatus(Integer contentTypeId, Integer objId, Integer userId){
        List<LikeRecord> likeRecords = likeRecordMapper.selectBycontentTypeIdAndobjIDAnduserId(contentTypeId, objId, userId);
        if(!likeRecords.isEmpty()){
            return "active";
        }
        return "";
    }

    public Integer getLikeNum(Integer contentTypeId, Integer objId){
        List<LikeCount> likeCounts = likeCountMapper.selectBycontentTypeIdAndobjIDAnduserId(contentTypeId, objId);
        if(!likeCounts.isEmpty()){
            return likeCounts.get(0).getLikedNum();
        }
        return 0;
    }

    public List<LikeRecord> selectBycontentTypeIdAndobjIDAnduserIdLikeRecord(Integer contentTypeId, Integer objId, Integer userId){
        return likeRecordMapper.selectBycontentTypeIdAndobjIDAnduserId(contentTypeId, objId, userId);
    }

    public int deleteByPrimaryKey(Integer id){
        return likeRecordMapper.deleteByPrimaryKey(id);
    }

    public LikeRecordBo getORCreateLikeRecordBo(Integer contentTypeId, Integer objId, Integer userId){
        LikeRecordBo likeRecordBo = new LikeRecordBo();
        List<LikeRecord> likeRecords = likeRecordMapper.selectBycontentTypeIdAndobjIDAnduserId(contentTypeId, objId, userId);
        if(!likeRecords.isEmpty()){
            //已经点赞过
            likeRecordBo.setLikeRecord(likeRecords.get(0));
            likeRecordBo.setCreated(false);
        }else{
            //未点赞过
            LikeRecord likeRecord = new LikeRecord();
            likeRecord.setContentTypeId(contentTypeId);
            likeRecord.setObjectId(objId);
            likeRecord.setUserId(userId);
            likeRecord.setLikedTime(new Date(System.currentTimeMillis()));
            likeRecordMapper.insertSelective(likeRecord);
            likeRecordBo.setLikeRecord(likeRecord);
            likeRecordBo.setCreated(true);
        }
        return likeRecordBo;
    }

    public LikeCountBo getORCreateLikeCountBo(Integer contentTypeId, Integer objId){
        LikeCountBo likeCountBo = new LikeCountBo();
        List<LikeCount> likeCounts = likeCountMapper.selectBycontentTypeIdAndobjIDAnduserId(contentTypeId, objId);
        if(!likeCounts.isEmpty()){
            //有点赞数量，直接获取
            likeCountBo.setLikeCount(likeCounts.get(0));
            likeCountBo.setCreated(false);
        }else{
            //有点赞数量，创建并返回
            LikeCount likeCount = new LikeCount();
            likeCount.setContentTypeId(contentTypeId);
            likeCount.setObjectId(objId);
            likeCount.setLikedNum(0);
            likeCountMapper.insertSelective(likeCount);
            likeCountBo.setLikeCount(likeCount);
            likeCountBo.setCreated(true);
        }
        return likeCountBo;
    }

    public int updateByPrimaryKeySelectiveLikeCount(LikeCount row){
        return likeCountMapper.updateByPrimaryKeySelective(row);
    }
}
