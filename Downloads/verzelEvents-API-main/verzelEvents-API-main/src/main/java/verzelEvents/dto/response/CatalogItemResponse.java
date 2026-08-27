package verzelEvents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CatalogItemResponse {
    private String externalId;
    private String titulo;
    private String posterUrl;
    private String dataLancamento;
}