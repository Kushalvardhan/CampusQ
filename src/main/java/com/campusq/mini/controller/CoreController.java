package com.campusq.mini.controller;

import com.campusq.mini.model.Answer;
import com.campusq.mini.model.Question;
import com.campusq.mini.model.Student;
import com.campusq.mini.repository.AnswerRepository;
import com.campusq.mini.repository.QuestionRepository;
import com.campusq.mini.repository.StudentRepository;
import com.campusq.mini.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("core")
public class CoreController {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionService questionService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AnswerRepository answerRepository;
    @RequestMapping(value = {"/ask_question"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView askQuestion(@RequestParam(value = "post", required = false) String post,
                                    @RequestParam(value = "fail", required = false) String fail){

        ModelAndView modelAndView = new ModelAndView("ask.html");
        modelAndView.addObject("question", new Question());
        String errorM = null;
        if(fail != null){
            errorM = "Failed to post! Please check and try again.";
        }

        modelAndView.addObject("errorM", errorM);
        return modelAndView;
    }

    @RequestMapping(value={"/post_question"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String postNewQuestion(@Valid @ModelAttribute("question") Question question, Errors errors, Authentication authentication){
        if(errors.hasErrors()){
            return "redirect:/core/ask_question?fail=true";
        }

        Student student = studentRepository.findByStudentId(authentication.getName());
        question.setStudent(student);
        questionRepository.save(question);

        return "redirect:/core/home_view?post=true";
    }

    @RequestMapping(value={"/board"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String viewBoard(@RequestParam(value = "tag", defaultValue = "Campus") String tag,
                            @RequestParam(value = "page", defaultValue = "1") int page, Model model){
        Page<Question> questions = questionService.findBoardQuestions(tag, page);
        model.addAttribute("questions", questions.getContent());
        model.addAttribute("selectedTag", tag);
        model.addAttribute("currentPage", questions.getNumber()+1);
        model.addAttribute("totalPages", questions.getTotalPages());
        return "board";
    }

    @RequestMapping(value={"/home_view"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView viewHome(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(value = "post", required = false) String post,
                                 @RequestParam(value = "adminDel", required = false) String adminDel){

        String confirmM = null;
        if(post != null) {
            confirmM = "Question Posted! Wait for the Answers.";
        }else if(adminDel != null){
            confirmM = "Question Deleted! (ADMIN CONTROL)";
        }

        Page<Question> questions = questionService.findQuestions(page);
        ModelAndView modelAndView = new ModelAndView("home.html");
        modelAndView.addObject("questions", questions.getContent());
        modelAndView.addObject("currentPage", questions.getNumber()+1);
        modelAndView.addObject("totalPages", questions.getTotalPages());
        modelAndView.addObject("confirmM", confirmM);
        return modelAndView;
    }

    @RequestMapping(value={"/save_answer"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String saveAnswer(@RequestParam(value = "questionId", required = true) int questionId,
                             @RequestParam(value = "answerText", required = true, defaultValue = "") String answerText,
                             Authentication authentication) {

        Answer answer = new Answer();
        answer.setDescription(answerText);
        if(answer.getDescription().isBlank()){
            return  "redirect:/core/home_view";
        }

        Question question = questionRepository.findByQuestionId(questionId);
        answer.setQuestion(question);
        Student student = studentRepository.findByStudentId(authentication.getName());
        answer.setStudent(student);

        answerRepository.save(answer);

        return "redirect:/core/view_answers?questionId=" + question.getQuestionId() + "&post=true";
    }

    @RequestMapping(value={"/save_answer_from_Board"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String saveAnswerFromBoard(@RequestParam(value = "questionId", required = true) int questionId,
                             @RequestParam(value = "answerText", required = true, defaultValue = "") String answerText,
                             @RequestParam(value = "selectedTag", required = true, defaultValue = "Campus") String selectedTag,
                             Authentication authentication) {

        Answer answer = new Answer();
        answer.setDescription(answerText);
        if(answer.getDescription().isBlank()){
            return "redirect:/core/board?tag=" + selectedTag;
        }

        Question question = questionRepository.findByQuestionId(questionId);
        answer.setQuestion(question);
        Student student = studentRepository.findByStudentId(authentication.getName());
        answer.setStudent(student);

        answerRepository.save(answer);

        return "redirect:/core/board?tag=" + selectedTag; // Redirect to the board page after submitting the answer
    }

    @RequestMapping(value={"/view_answers"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView viewAnswers(@RequestParam(value = "questionId", required = true) int questionId,
                                    @RequestParam(value = "post", required = false) String post){

        String confirmM = null;
        if(post != null) {
            confirmM = "Answer Posted!";
        }

        ModelAndView modelAndView = new ModelAndView("answers.html");
        Question question = questionRepository.findByQuestionId(questionId);
        List<Answer> answers = answerRepository.findByQuestionQuestionId(questionId);

        modelAndView.addObject("question", question);
        modelAndView.addObject("answers", answers);
        modelAndView.addObject("confirmM", confirmM);
        return modelAndView;
    }

    @RequestMapping(value={"/more"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView moreOptions(@RequestParam(value = "questionId", required = true) int questionId){
        ModelAndView modelAndView = new ModelAndView("more.html");
        Question question = questionRepository.findByQuestionId(questionId);

        modelAndView.addObject("question", question);
        return modelAndView;
    }

    @RequestMapping(value={"/delete_question"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteQuestion(@RequestParam(value = "questionId", required = true) int questionId, Authentication authentication){

        Question question = questionRepository.findByQuestionId(questionId);

        if (!authentication.getName().equals(question.getStudent().getStudentId())) {
            throw new SecurityException("You are not allowed to perform this operation! Current User: " + authentication.getName() + ", Author ID: " + question.getStudent().getStudentId());
        }


        List<Answer> answers = answerRepository.findByQuestionQuestionId(questionId);

        for (Answer answer : answers) {
            answerRepository.delete(answer);
        }

        questionRepository.delete(question);

        return "redirect:/profile";
    }

    @RequestMapping(value={"/delete_answer"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteAnswer(@RequestParam(value = "answerId", required = true) int answerId, Authentication authentication){

        Answer answer = answerRepository.findByAnswerId(answerId);

        if (!authentication.getName().equals(answer.getStudent().getStudentId())) {
            throw new SecurityException("You are not allowed to perform this operation! Current User: " + authentication.getName() + ", Author ID: " + answer.getStudent().getStudentId());
        }

        answerRepository.delete(answer);

        return "redirect:/profile";
    }

}
