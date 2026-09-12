package verzelEvents.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import verzelEvents.dto.response.AssentoResponse;
import verzelEvents.entity.Assento;
import verzelEvents.exception.ResourceNotFoundException;
import verzelEvents.repository.AssentoRepository;
import verzelEvents.repository.EventoRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssentoService {

    private final AssentoRepository assentoRepository;
    private final EventoRepository eventoRepository;

    @Transactional(readOnly = true)
    public List<AssentoResponse> listByEvento(UUID eventoId) {
        eventoRepository.findById(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado: " + eventoId));

        return assentoRepository.findByEventoIdOrderByCodigoAsc(eventoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AssentoResponse toResponse(Assento assento) {
        return new AssentoResponse(
                assento.getId(),
                assento.getCodigo(),
                assento.getStatus()
        );
    }
}