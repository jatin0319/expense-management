package com.service.expensemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableWebMvc
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private ApiKey apiKey(){
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private List<SecurityReference> securityReferences(){
        AuthorizationScope scopes = new AuthorizationScope("global", "accessEverything");
        return Arrays.asList(new SecurityReference("JWT", new AuthorizationScope[]{scopes}));
    }

    private List<SecurityContext> securityContexts(){
        return Arrays.asList(SecurityContext.builder()
                .securityReferences(securityReferences())
                .build());
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.service.expensemanagement"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getInfo())
                .securityContexts(securityContexts())
                .securitySchemes(Arrays.asList(apiKey()));
    }

    private ApiInfo getInfo() {
        return new ApiInfo("Expense Management", "This project is developed by Jatin Rawat","1.0","",new Contact("Jatin Rawat", "","jatinrwt19@gmail.com"),"License of Apis", "", Collections.emptyList());
    }
}