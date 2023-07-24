package com.campusq.mini.model;

import com.campusq.mini.annotation.FieldsValueMatch;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@Entity

@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "confirmPwd",
                message = "Passwords do not match!"
        )
})

public class Student {

    @Id
    @NotBlank(message="ID Number must not be blank!")
    @Pattern(regexp = "^S18(0[0-9]{3}|1[01][0-9]{2}|1200)$", message = "Invalid student ID!")
    private String studentId;

    @NotBlank(message="First Name must not be blank!")
    @Size(min=3, message="First Name must be at least 3 characters long!")
    private String firstName;

    @NotBlank(message="Last Name must not be blank!")
    @Size(min=3, message="Last Name must be at least 3 characters long!")
    private String lastName;

    private String branch;

    @NotBlank(message="Email must not be blank!")
    @Pattern(regexp = "^s18(0[0-9]{3}|1[01][0-9]{2}|1200)@rguktsklm\\.ac\\.in$", message = "Invalid student email")
    private String email;

    @NotBlank(message="Password must be 8 to 12 characters long!")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=_])(?=\\S+$).{8,12}$", message = "Invalid password")
    private String password;

    @NotBlank(message="Password must be 8 to 12 characters long!")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=_])(?=\\S+$).{8,12}$", message = "Invalid Confirm password")
    @Transient
    private String confirmPwd;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, targetEntity = Roles.class)
    @JoinColumn(name = "role_id", referencedColumnName = "roleId", nullable = false)
    private Roles roles;

    @Override
    public String toString() {
        return studentId + " " + firstName;
    }

}


