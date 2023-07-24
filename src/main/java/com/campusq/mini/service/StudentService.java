package com.campusq.mini.service;

import com.campusq.mini.model.Roles;
import com.campusq.mini.model.Student;
import com.campusq.mini.repository.RolesRepository;
import com.campusq.mini.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean saveStudentDetails(Student student){
        boolean isSaved = false;
        Roles role = rolesRepository.getByRoleName("STUDENT");
        student.setRoles(role);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student = studentRepository.save(student);

        if(student != null && student.getStudentId() != null){
            isSaved = true;
        }

        return isSaved;
    }
}
