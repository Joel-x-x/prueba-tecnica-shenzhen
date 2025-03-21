package com.back.infrastructure.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
  @Bean
  public OpenAPI customOpenAPI(){
    return new OpenAPI()
      .components(new Components()
          .addSecuritySchemes("bearerAuth",
            new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")))
      .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
      .info(new Info()
          .title("API Gestion de posts")
          .version("1.0")
          .description("Documentación interactiva de la API para gestionar " +
                  "posts y usuarios"));
  }
}
