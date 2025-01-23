package com.yevhenii.bezpalchenko.self_learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.yevhenii.bezpalchenko.self_learning.Model")
public class SelfLearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(SelfLearningApplication.class, args);
	}

}

//https://www.youtube.com/watch?v=KxqlJblhzfI
//https://github.com/ali-bouali/spring-boot-3-jwt-security/blob/main/src/main/java/com/alibou/security/demo/AdminController.java
