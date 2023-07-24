package com.campusq.mini.service;

import com.campusq.mini.model.Student;
import com.campusq.mini.repository.QuestionRepository;
import com.campusq.mini.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.campusq.mini.model.Question;

import java.util.List;

@Slf4j
@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Page<Question> findQuestions(int pageNum){
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNum-1, pageSize, Sort.by("createdAt").descending());

        Page<Question> questions = questionRepository.findAll(pageable);

        return questions;
    }

    public Page<Question> findBoardQuestions(String tag, int pageNum){
        int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNum-1, pageSize, Sort.by("createdAt").descending());

        Page<Question> questions = questionRepository.findAllByTag(tag, pageable);

        return questions;
    }

    public Page<Question> findProfileQuestions(Student student, int pageNum){
        int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNum-1, pageSize, Sort.by("createdAt").descending());

        Page<Question> questions = questionRepository.findByStudent(student, pageable);

        return questions;
    }
}
