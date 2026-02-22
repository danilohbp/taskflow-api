package com.danilohbp.taskflowapi.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI taskflowOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Taskflow API")
                        .description("Task management API (CQRS light) - Users & Tasks")
                        .version("v1"));
    }
}
