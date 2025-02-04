package org.js.swiftcodes.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

@OpenAPIDefinition(servers = @Server(url = "/", description = "https://localhost:8080"))
@Configuration
@RequiredArgsConstructor
public class SwaggerOpenAPIConfig {
    @Bean
    public BuildProperties createProperties() {
        return new org.springframework.boot.info.BuildProperties(new Properties());
    }

    @Bean
    public OpenAPI customOpenAPI() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(new ClassPathResource("swift-codes-open-api.yaml").getInputStream(), OpenAPI.class);
    }

    private Info apiInfo() {
        return new Info().title("SWIFT Codes")
            .description("API for Animals Shelter management.")
            .version(createProperties().getVersion());
    }
}
