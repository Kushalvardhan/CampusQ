package com.campusq.mini.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FeedController {
    @RequestMapping(value={"/board"})
    public String viewQuestions(){
        return "board.html";
    }
}
