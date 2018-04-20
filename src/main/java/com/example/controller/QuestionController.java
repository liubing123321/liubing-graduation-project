package com.example.controller;

/**
 * Created by Administrator on 2017/5/16.
 */

import com.example.aspect.LogAspect;
import com.example.dao.QuestionDAO;
import com.example.model.*;
import com.example.service.CommentService;
import com.example.service.LikeService;
import com.example.service.QuestionService;
import com.example.service.UserService;
import com.example.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private static final Logger logger= LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;

    @RequestMapping(value="/question/add",method={RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,@RequestParam("content") String content){
            try {
                 Question question =new Question();
                question.setContent(content);
                question.setTitle(title);
                question.setCreatedDate(new Date());
                question.setCommentCount(0);
                if(hostHolder.getUser()==null){
                    question.setUserId(WendaUtil.ANONYMOUS_USERID);//付给匿名用户一个ID
                }else{
                    question.setUserId(hostHolder.getUser().getId());
                }

                if( questionService.addQuestion(question)>0){
                        return WendaUtil.getJSONString(0);
                }
            }catch(Exception e){
                    logger.error("增加题目失败"+e.getMessage());
            }
            return WendaUtil.getJSONString(1,"失败");
    }
    
    @RequestMapping(value="/question/{qid}")
    public String questionDetail(Model model,@PathVariable("qid") int qid){
        Question question=questionService.getById(qid);
        model.addAttribute("question",question);
       // model.addAttribute("user",userService.getUser(question.getUserId()));
        List<Comment> commentList=commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments=new ArrayList<ViewObject>();
        for(Comment comment:commentList){
            ViewObject vo=new ViewObject();//视图对象
            vo.set("comment",comment);
            if(hostHolder.getUser()==null){
                vo.set("liked",0);
            }else{
                vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
            }
            vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
            vo.set("user",userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);
        return "detail";
    }
}
