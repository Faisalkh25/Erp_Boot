package com.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.LoginRequest;
import com.erp.dto.LoginResponse;
import com.erp.model.Employee;
import com.erp.repository.EmployeeRepository;
import com.erp.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmployeeRepository employeeRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(String.valueOf(loginRequest.getEmpCode()),
                        loginRequest.getPassword()));

        // if authentication successfull

        Employee employee = employeeRepo.findByEmpCode(loginRequest.getEmpCode());
        if (employee == null) {
            throw new UsernameNotFoundException("Employee not Found");
        }

        String token = jwtUtil.generateToken(String.valueOf(employee.getEmpCode()), employee.getEmpId());
        String role = employee.getRole().getRole_name();

        return ResponseEntity.ok(new LoginResponse(token, role, employee.getEmpCode()));
    }

}
