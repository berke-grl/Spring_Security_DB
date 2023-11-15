package com.example.security.Config;

import com.example.security.Entity.Employee;
import com.example.security.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class UsernamePwdProvider implements AuthenticationProvider {
    EmployeeRepository repository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UsernamePwdProvider(EmployeeRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        List<GrantedAuthority> roles = new ArrayList<>();
        Employee employee = repository.findEmployeeByEmail(username);
        if (employee != null) {
            roles.add(new SimpleGrantedAuthority(employee.getRole()));
            return new UsernamePasswordAuthenticationToken(username, password, roles);
        } else {
            throw new BadCredentialsException("Invalid User Details");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
