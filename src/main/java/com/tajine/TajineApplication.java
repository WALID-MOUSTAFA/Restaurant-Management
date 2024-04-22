package com.tajine;

import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TajineApplication {

	public static void main(String[] args) {

		//SpringApplication.run(TajineApplication.class, args);
		Application.launch(TajineFXApplication.class, args);
		System.out.println();
	}

}
