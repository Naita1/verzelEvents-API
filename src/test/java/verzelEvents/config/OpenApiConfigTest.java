package verzelEvents.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {

    @Test
    @DisplayName("Deve conter as anotações corretas de OpenAPIDefinition e SecurityScheme")
    void deveConterAnotacoesConfiguradas() {
        Class<OpenApiConfig> configClass = OpenApiConfig.class;

        assertThat(configClass.isAnnotationPresent(OpenAPIDefinition.class)).isTrue();
        assertThat(configClass.isAnnotationPresent(SecurityScheme.class)).isTrue();

        OpenAPIDefinition openAPIDefinition = configClass.getAnnotation(OpenAPIDefinition.class);
        assertThat(openAPIDefinition.info().title()).isEqualTo("Verzel Events API");
        assertThat(openAPIDefinition.info().version()).isEqualTo("v1");
        assertThat(openAPIDefinition.servers()).hasSize(2);

        SecurityScheme securityScheme = configClass.getAnnotation(SecurityScheme.class);
        assertThat(securityScheme.name()).isEqualTo("bearerAuth");
        assertThat(securityScheme.scheme()).isEqualTo("bearer");
        assertThat(securityScheme.bearerFormat()).isEqualTo("JWT");
    }
}