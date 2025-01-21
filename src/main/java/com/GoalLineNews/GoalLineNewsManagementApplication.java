package com.GoalLineNews;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
public class GoalLineNewsManagementApplication {
	public enum Role {
		USER,
		WRITER,
		ADMIN
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(GoalLineNewsManagementApplication.class, args);
	}
}
