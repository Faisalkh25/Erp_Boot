package com.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.LoginRequest;
import com.erp.dto.LoginResponse;
import com.erp.model.Employee;
import com.erp.repository.EmployeeRepository;
import com.erp.service.TokenBlacklistService;
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

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        // authManager.authenticate(
        // new
        // UsernamePasswordAuthenticationToken(String.valueOf(loginRequest.getEmpCode()),
        // loginRequest.getPassword()));

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmpCode(), loginRequest.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // if authentication successfull

        Employee employee = employeeRepo.findByEmpCode(loginRequest.getEmpCode());
        if (employee == null) {
            throw new UsernameNotFoundException("Employee not Found");
        }

        // String token = jwtUtil.generateToken(String.valueOf(employee.getEmpCode()),
        // employee.getEmpId());
        String token = jwtUtil.generateToken(userDetails, employee.getEmpId());

        String role = employee.getRole().getRole_name();

        return ResponseEntity.ok(new LoginResponse(token, role, employee.getEmpCode(), employee.getEmpId()));
    }

    // logout

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklist(token);
        }
        return ResponseEntity.ok("Logged out successfully!");
    }

}
