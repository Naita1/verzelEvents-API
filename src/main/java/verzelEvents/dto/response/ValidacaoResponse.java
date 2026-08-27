package verzelEvents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidacaoResponse {
    private String resultado;
    private String mensagem;
}