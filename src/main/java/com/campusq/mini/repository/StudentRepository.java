package com.campusq.mini.repository;

import com.campusq.mini.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, String> {
    Student findByStudentId(String studentId);
}
