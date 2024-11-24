package org.example.backend.controller;

import org.example.backend.entity.Test;
import org.example.backend.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:3000") // Adjust based on your frontend's origin
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public ResponseEntity<List<Test>> findAll() {
        List<Test> tests = testService.findAll();
        return ResponseEntity.ok(tests);
    }
}
