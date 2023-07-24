package com.campusq.mini.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AskController {
    @RequestMapping(value={"/ask"})
    public String askquestion(){
        return "ask.html";
    }
}
