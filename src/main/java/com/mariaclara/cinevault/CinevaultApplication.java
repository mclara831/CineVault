package com.mariaclara.cinevault;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Cine Vault API", version = "1",
		description = "API implementa aconceitos de autenticação e autorização via Tokens e consume endpoints de API externas."))
@EnableFeignClients
@EnableJpaAuditing
public class CinevaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinevaultApplication.class, args);
	}

}
