package verzelEvents.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Verzel Events API",
        version = "v1",
        description = "API para a plataforma de eventos e ingressos Verzel Events. " +
                      "Permite a criação de eventos, reserva de assentos, pagamento simulado e validação de ingressos."
    )
)
@SecurityScheme(
    name = "bearerAuth",
    description = "Autenticação via JWT. Insira o token precedido por 'Bearer '.",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}