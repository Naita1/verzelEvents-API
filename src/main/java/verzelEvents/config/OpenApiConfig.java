package verzelEvents.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Verzel Events API",
        version = "v1",
        description = "API REST para a plataforma de eventos e ingressos Verzel Events. " +
                      "Permite a criação de eventos, reserva de assentos, pagamento simulado e validação de ingressos.",
        contact = @Contact(
            name = "Taina Ribeiro",
            email = "tainaribeir1930@gmail.com",
            url = "https://github.com/Naita1"
        ),
        license = @License(
            name = "Licença de Uso Restrito"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Servidor Local"),
        @Server(url = "https://api.verzel-events.com", description = "Servidor de Produção")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "Autenticação via JWT. Insira APENAS o token, o Swagger se encarrega de adicionar o prefixo 'Bearer'.",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}