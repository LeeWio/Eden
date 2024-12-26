package com.megatronix.eden.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Swagger configuration class for setting up Swagger 3 documentation for the
 * Eden API.
 * This configuration provides basic API information and can be expanded with
 * additional setup for
 * authentication, global responses, etc.
 */
@Configuration
public class SwaggerConfiguration {

  /**
   * Configures the OpenAPI documentation for the Eden API.
   * 
   * @return OpenAPI object with custom information for the API documentation
   */
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Eden API")
            .version("1.0")
            .description("Eden API documentation providing detailed information about endpoints and data models.")
            .termsOfService("https://www.megatronix.com/terms")
            .contact(new Contact()
                .name("Eden Support Team")
                .email("support@megatronix.com")
                .url("https://www.megatronix.com"))
            .license(new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT")))
        .components(new Components()
            .addSecuritySchemes("bearerAuth", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")));
  }
}
