package verzelEvents.service;

import tools.jackson.databind.JsonNode;
import verzelEvents.dto.response.CatalogItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TmdbService {

    private final RestClient tmdbRestClient;

    public List<CatalogItemResponse> searchMovies(String query) {
        JsonNode response = tmdbRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("query", query)
                        .queryParam("language", "pt-BR")
                        .build())
                .retrieve()
                .body(JsonNode.class);

        List<CatalogItemResponse> resultados = new ArrayList<>();
        if (response != null && response.has("results")) {
            for (JsonNode item : response.get("results")) {
                resultados.add(new CatalogItemResponse(
                        item.get("id").asText(),
                        item.get("title").asText(),
                        item.hasNonNull("poster_path")
                                ? "https://image.tmdb.org/t/p/w500" + item.get("poster_path").asText()
                                : null,
                        item.hasNonNull("release_date") ? item.get("release_date").asText() : null
                ));
            }
        }
        return resultados;
    }
}