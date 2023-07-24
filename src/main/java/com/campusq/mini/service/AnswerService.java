package com.campusq.mini.service;


import com.campusq.mini.model.Answer;
import com.campusq.mini.model.Question;
import com.campusq.mini.model.Student;
import com.campusq.mini.repository.AnswerRepository;
import com.campusq.mini.repository.QuestionRepository;
import com.campusq.mini.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnswerService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public Page<Answer> findMyAnswers(Student student, int pageNum){
        int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNum-1, pageSize, Sort.by("createdAt").descending());

        Page<Answer> answers = answerRepository.findByStudent(student, pageable);

        return answers;
    }
}
