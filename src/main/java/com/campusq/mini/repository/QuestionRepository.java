package com.campusq.mini.repository;

import com.campusq.mini.model.Roles;
import com.campusq.mini.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.campusq.mini.model.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findByQuestionId(int qId);
    Page<Question> findByStudent(Student student, Pageable pageable);

    List<Question> findAllByOrderByCreatedAtDesc();

    Page<Question> findAllByTag(String tag, Pageable pageable);

    Page<Question> findAll(Pageable pageable);

    void deleteByQuestionId(Integer id);

}
