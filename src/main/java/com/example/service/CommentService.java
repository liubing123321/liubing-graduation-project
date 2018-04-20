package com.example.service;

import com.example.dao.CommentDAO;
import com.example.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentsByEntity(int entityId, int entityType){
            return commentDAO.selectCommentByEntity(entityId,entityType);
    }

    public int addComment(Comment comment){
            comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));//增加评论去掉html标签
            comment.setContent(sensitiveService.filter(comment.getContent()));//评论内容敏感词过滤
            return commentDAO.addComment(comment)>0?comment.getId() : 0;
    }
    public int getCommentCount(int entityId, int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }

    public int getUserCommentCount(int userId) {
        return commentDAO.getUserCommentCount(userId);
    }
    public boolean deleteComment(int commentId){
        return commentDAO.updateStatus(commentId,1)>0;
    }

    public Comment getCommentById(int id){
        return commentDAO.getCommentById(id);
    }
}
