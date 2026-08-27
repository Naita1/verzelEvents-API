package verzelEvents.service;

import verzelEvents.dto.request.CreateEventRequest;
import verzelEvents.dto.response.EventoResponse;
import verzelEvents.exception.ResourceNotFoundException;
import verzelEvents.entity.*;
import verzelEvents.repository.AssentoRepository;
import verzelEvents.repository.EventoRepository;
import verzelEvents.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final AssentoRepository assentoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public EventoResponse createEvent(CreateEventRequest request, String organizadorEmail) {
        Usuario organizador = usuarioRepository.findByEmail(organizadorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Organizador não encontrado"));

        Evento evento = Evento.builder()
                .organizador(organizador)
                .titulo(request.getTitulo())
                .tipo(request.getTipo())
                .dataHora(request.getDataHora())
                .local(request.getLocal())
                .capacidade(request.getCapacidade())
                .preco(request.getPreco())
                .imagemUrl(request.getImagemUrl())
                .build();

        eventoRepository.save(evento);

        for (int i = 1; i <= request.getCapacidade(); i++) {
            Assento assento = Assento.builder()
                    .evento(evento)
                    .codigo("A" + i)
                    .status(AssentoStatus.LIVRE)
                    .build();
            assentoRepository.save(assento);
        }

        return toResponse(evento);
    }

    public List<EventoResponse> listEvents() {
        return eventoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EventoResponse getEventDetails(UUID id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado: " + id));
        return toResponse(evento);
    }

    public List<EventoResponse> listMyEvents(String organizadorEmail) {
        Usuario organizador = usuarioRepository.findByEmail(organizadorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Organizador não encontrado"));
        return eventoRepository.findByOrganizadorId(organizador.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private EventoResponse toResponse(Evento evento) {
        return new EventoResponse(
                evento.getId(),
                evento.getTitulo(),
                evento.getTipo(),
                evento.getDataHora(),
                evento.getLocal(),
                evento.getCapacidade(),
                evento.getPreco(),
                evento.getOrganizador().getNome(),
                evento.getImagemUrl()
        );
    }
}