package com.loja.loja_produtos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Catalogo A3")
                        .version("v1")
                        .description("API de Catalogo de Produtos")
                        .license(
                                new License()
                                        .name("Apache 2.0")
                        ));
    }
}
