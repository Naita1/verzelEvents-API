package verzelEvents.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import verzelEvents.dto.response.AssentoResponse;
import verzelEvents.repository.AssentoRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssentoService {

    private final AssentoRepository assentoRepository;

    @Transactional(readOnly = true)
    public List<AssentoResponse> listByEvento(UUID eventoId) {
        return assentoRepository.findByEventoId(eventoId)
                .stream()
                .map(assento -> new AssentoResponse(
                        assento.getId(),
                        assento.getCodigo(),
                        assento.getStatus()
                ))
                .toList();
    }
}