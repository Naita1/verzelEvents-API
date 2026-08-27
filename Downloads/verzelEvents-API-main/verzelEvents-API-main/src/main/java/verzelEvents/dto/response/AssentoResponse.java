package verzelEvents.dto.response;

import verzelEvents.entity.AssentoStatus;

public record AssentoResponse(
        java.util.UUID id,
        String codigo,
        AssentoStatus status
) {}