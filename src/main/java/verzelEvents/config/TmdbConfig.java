package verzelEvents.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TmdbConfig {

    @Value("${app.tmdb.api-key}")
    private String apiKey;

    @Bean
    public RestClient tmdbRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }
}