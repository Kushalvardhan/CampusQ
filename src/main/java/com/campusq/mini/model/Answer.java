package com.campusq.mini.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "answers")
public class Answer extends BaseClass{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private int answerId;

    @NotBlank(message = "Cant submit empty block!")
    private String description;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = Student.class)
    @JoinColumn(name = "created_by", referencedColumnName = "studentId", nullable = false)
    private Student student;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = Question.class)
    @JoinColumn(name = "mother_id", referencedColumnName = "questionId", nullable = false)
    private Question question;
}
