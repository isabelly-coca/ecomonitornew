package com.ecotrack.ecomonitor.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Eco Monitor").version("1.0")
                .description("API para gerenciamento de Estações, seus sensores e suas leituras\n" +
                "Aqui detalhamos os endpoints disponíveis na API, suas funcionalidades e como utilizá-los."));
    }
}
