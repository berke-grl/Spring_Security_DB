package com.example.security.Controller;

import com.example.security.Entity.Employee;
import com.example.security.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {
    PasswordEncoder passwordEncoder;
    EmployeeRepository repository;

    @Autowired
    public RegisterController(PasswordEncoder passwordEncoder, EmployeeRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }


    @PostMapping("/save")
    public ResponseEntity<String> registerUser(@RequestBody Employee employee) {
        String email = employee.getEmail();
        String password = passwordEncoder.encode(employee.getPassword());
        boolean employeeIsExist = repository.findEmployeeByEmail(email) != null;
        if (!employeeIsExist) {
            repository.save(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body("New Employee Registered Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This Email address is already exist");
        }
    }
}
