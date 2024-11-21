package org.example.ams_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AmsBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmsBeApplication.class, args);
    }

}
