package io.javabrains.springbootstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "restApiController"} )
public class RestApiApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SpringApplication.run(RestApiApp.class, args);

	}

}
