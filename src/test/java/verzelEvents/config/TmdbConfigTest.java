package verzelEvents.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

class TmdbConfigTest {

    @Test
    @DisplayName("Deve construir o RestClient do TMDb com as propriedades injetadas")
    void deveConstruirRestClientComSucesso() {
        TmdbConfig config = new TmdbConfig();
        ReflectionTestUtils.setField(config, "apiKey", "test_api_key_123");
        ReflectionTestUtils.setField(config, "baseUrl", "http://localhost:8089");

        RestClient restClient = config.tmdbRestClient();

        assertThat(restClient).isNotNull();
    }
}