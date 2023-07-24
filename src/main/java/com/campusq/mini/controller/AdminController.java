package com.campusq.mini.controller;

import com.campusq.mini.model.Answer;
import com.campusq.mini.model.Question;
import com.campusq.mini.repository.AnswerRepository;
import com.campusq.mini.repository.QuestionRepository;
import com.campusq.mini.repository.StudentRepository;
import com.campusq.mini.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionService questionService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AnswerRepository answerRepository;

    @RequestMapping(value={"/admin_delete_question_home"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String adminDeleteQuestion(@RequestParam(value = "questionId", required = true) int questionId, Authentication authentication){

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            Question question = questionRepository.findByQuestionId(questionId);
            List<Answer> answers = answerRepository.findByQuestionQuestionId(questionId);

            for (Answer answer : answers) {
                answerRepository.delete(answer);
            }

            questionRepository.delete(question);

            return "redirect:/core/home_view?adminDel=true";
        } else {
            throw new SecurityException("You are not allowed to perform this operation. Admin access required.");
        }
    }

    @RequestMapping(value={"/admin_delete_answer"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String adminDeleteAnswer(@RequestParam(value = "questionId", required = true) int questionId,
                                    @RequestParam(value = "answerId", required = true) int answerId, Authentication authentication){

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            Answer answer = answerRepository.findByAnswerId(answerId);
            answerRepository.delete(answer);

            return "redirect:/core/view_answers?questionId=" + questionId;

        } else {
            throw new SecurityException("You are not allowed to perform this operation. Admin access required.");
        }
    }

}
