package org.example.backend.service;

import org.example.backend.entity.Test;
import org.example.backend.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestService.class);
    private final TestRepository testRepository;

    @Autowired
    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public List<Test> findAll() {
        return testRepository.findAll();
    }
}