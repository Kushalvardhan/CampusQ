package com.campusq.mini.controller;

import com.campusq.mini.model.Answer;
import com.campusq.mini.model.Question;
import com.campusq.mini.model.Student;
import com.campusq.mini.repository.QuestionRepository;
import com.campusq.mini.repository.StudentRepository;
import com.campusq.mini.service.AnswerService;
import com.campusq.mini.service.QuestionService;
import com.campusq.mini.service.StudentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionService questionService;

    @Autowired
    AnswerService answerService;

    @RequestMapping(value={"/register"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String newUser(@RequestParam(value = "success", required = false) String success,
                          @RequestParam(value = "failed", required = false) String failed,
                          Model model){
        String message = null;

        if(success != null){
            message = "Registration Successful! Login with your credentials.";
        }else if(failed != null){
            message = "Registration Failed! Try again.";
        }

        model.addAttribute("message", message);
        model.addAttribute("student", new Student());

        return "register.html";
    }

    @RequestMapping(value = {"/register_user"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String registerStudent(@Valid @ModelAttribute("student") Student student, Errors errors){
        if(errors.hasErrors()){
            return "register.html";
        }
        boolean isSaved = studentService.saveStudentDetails(student);

        if(isSaved){
            return "redirect:/login?register=true";
        } else {
            return "redirect:/register?failed=true";
        }
    }

    @RequestMapping(value = {"/profile"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView studentProfile(@RequestParam(defaultValue = "1") int page, Authentication authentication){
        ModelAndView modelAndView = new ModelAndView("profile.html");

        Student student = studentRepository.findByStudentId(authentication.getName());
        modelAndView.addObject("user", student);

        Page<Question> questions = questionService.findProfileQuestions(student, page);
        Page<Answer> answers = answerService.findMyAnswers(student, page);

        modelAndView.addObject("userQuestions", questions.getContent());
        modelAndView.addObject("userAnswers", answers.getContent());
        modelAndView.addObject("QueCurrentPage", questions.getNumber()+1);
        modelAndView.addObject("QueTotalPages", questions.getTotalPages());
        modelAndView.addObject("AnsCurrentPage", answers.getNumber()+1);
        modelAndView.addObject("AnsTotalPages", answers.getTotalPages());

        return modelAndView;
    }

}
