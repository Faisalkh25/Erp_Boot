package com.erp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erp.dto.LeaveApplicationDto;
import com.erp.model.LeaveApplication;
import com.erp.service.LeaveApplicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin("*")
public class LeaveApplicationRestController {

    private LeaveApplicationService leaveService;

    public LeaveApplicationRestController(LeaveApplicationService leaveService) {
        this.leaveService = leaveService;
    };

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<LeaveApplication> applyLeave(
            @RequestPart("leaveDto") String leaveDtoJson,
            @RequestPart(value = "file", required = false) MultipartFile file)
            throws IllegalArgumentException, JsonMappingException, JsonProcessingException {

        // convert JSON string to DTO
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LeaveApplicationDto dto = mapper.readValue(leaveDtoJson, LeaveApplicationDto.class);

        String attachmentPath = null;
        try {
            if (file != null && !file.isEmpty()) {
                attachmentPath = leaveService.saveAttachment(file);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        LeaveApplication leave = leaveService.applyLeave(dto, attachmentPath);
        return new ResponseEntity<>(leave, HttpStatus.CREATED);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveApplicationDto>> getLeavesByEmployee(@PathVariable int employeeId) {
        return ResponseEntity.ok(leaveService.getLeavesByEmployee(employeeId));
    }

}
