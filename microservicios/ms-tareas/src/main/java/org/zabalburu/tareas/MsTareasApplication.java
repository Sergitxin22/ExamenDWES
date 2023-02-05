package org.zabalburu.tareas;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MsTareasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTareasApplication.class, args);
	}
	
	@Bean
	public RestTemplate gettemplate() {
		return new RestTemplate();
	}

}
