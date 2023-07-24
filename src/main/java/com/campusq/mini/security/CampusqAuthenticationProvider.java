package com.campusq.mini.security;

import com.campusq.mini.model.Roles;
import com.campusq.mini.model.Student;
import com.campusq.mini.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CampusqAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String sId = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Student student = studentRepository.findByStudentId(sId);

        if(student != null && passwordEncoder.matches(pwd, student.getPassword())){
            return new UsernamePasswordAuthenticationToken(student.getStudentId(), pwd, getGrantedAuthorities(student.getRoles()));
        }else{
            throw new BadCredentialsException("Invalid Credentials!");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Roles roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+roles.getRoleName()));
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
