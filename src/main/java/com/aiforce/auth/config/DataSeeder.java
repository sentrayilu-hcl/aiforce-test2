package com.aiforce.auth.config;

import com.aiforce.auth.domain.UserAccount;
import com.aiforce.auth.repository.UserAccountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder {

    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserAccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void seed() {
        repository.findByUsernameIgnoreCase("customer1").orElseGet(() ->
                repository.save(new UserAccount("customer1", passwordEncoder.encode("ChangeMe123!"), true)));
    }
}
