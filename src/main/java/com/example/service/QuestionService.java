package com.example.service;

import com.example.dao.QuestionDAO;
import com.example.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */
@Service
public class QuestionService {
        @Autowired
        QuestionDAO questionDAO;

        @Autowired
        SensitiveService sensitiveService;

        public Question getById(int id){
            return questionDAO.getById(id);
        }


        public int addQuestion(Question question){
            //html标签过滤
            question.setContent(HtmlUtils.htmlEscape(question.getContent()));
            question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
            //敏感词过滤
            question.setContent(HtmlUtils.htmlEscape(sensitiveService.filter(question.getContent())));
            question.setTitle(HtmlUtils.htmlEscape(sensitiveService.filter(question.getTitle())));
            return questionDAO.addQuestion(question) > 0 ? question.getId():0;
        }

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }
    public int updateCommentCount(int id, int count) {
        return questionDAO.updateCommentCount(id, count);
    }
}
