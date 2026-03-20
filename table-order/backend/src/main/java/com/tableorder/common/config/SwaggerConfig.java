package com.tableorder.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwt = "Bearer JWT";
        return new OpenAPI()
                .info(new Info()
                        .title("Table Order API")
                        .description("테이블 오더 서비스 API")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(jwt))
                .components(new Components()
                        .addSecuritySchemes(jwt, new SecurityScheme()
                                .name(jwt)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
