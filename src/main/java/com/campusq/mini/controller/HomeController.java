package com.campusq.mini.controller;

import com.campusq.mini.model.Question;
import com.campusq.mini.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @Autowired
    QuestionRepository questionRepository;
    @RequestMapping(value={"", "/", "home"})
    public String displayHome(Model model){
        model.addAttribute("username", "RGUKTian");
        return "home.html";
    }
}


