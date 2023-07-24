package com.campusq.mini.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "questions")
public class Question extends BaseClass{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private int questionId;

    @NotBlank(message = "Cant submit empty block!")
    private String description;

    @NotBlank(message = "Cant submit with out tag selected!")
    private String tag;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = Student.class)
    @JoinColumn(name = "created_by", referencedColumnName = "studentId", nullable = false)
    private Student student;

    @Override
    public String toString() {
        return "qId=" + questionId +  "]";
    }

}
