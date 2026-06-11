package com.nexa.task.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nexa AI - Task Service")
                        .version("1.0.0")
                        .description("""
                                Serviço responsável pelo gerenciamento de produtividade
                                do ecossistema Nexa AI.
                                
                                Este serviço centraliza funcionalidades relacionadas a:
                                - Workspaces
                                - Tarefas
                                - Subtarefas
                                - Organização e acompanhamento de atividades
                                
                                Repositório GitHub:
                                https://github.com/felipesora/nexa-ai-task-service
                                """)
                        .contact(new Contact()
                                .name("Linkedin - Felipe Sora")
                                .url("https://www.linkedin.com/in/felipesora"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .schemaRequirement(
                        SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }
}
