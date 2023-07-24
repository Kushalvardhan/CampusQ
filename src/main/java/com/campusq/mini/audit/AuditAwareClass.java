package com.campusq.mini.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareClass")
public class AuditAwareClass implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor(){
        return Optional.ofNullable(
                SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }
}
