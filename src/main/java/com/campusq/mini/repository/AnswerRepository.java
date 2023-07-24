package com.campusq.mini.repository;

import com.campusq.mini.model.Answer;
import com.campusq.mini.model.Question;
import com.campusq.mini.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findByQuestionQuestionId(int questionId);

    void deleteByAnswerId(int id);

    Page<Answer> findByStudent(Student student, Pageable pageable);

    Answer findByAnswerId(int aId);
}
