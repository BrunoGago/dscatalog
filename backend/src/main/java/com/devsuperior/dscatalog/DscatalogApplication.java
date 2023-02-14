//1- Projeto criado usando o website (https://start.spring.io/), usando as dependencias Spring WEB, Spring Data JPA, H2 Database e PostgreSQL

package com.devsuperior.dscatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DscatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(DscatalogApplication.class, args);
	}

}
