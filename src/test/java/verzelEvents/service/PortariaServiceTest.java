package verzelEvents.service;

import verzelEvents.dto.request.ValidarIngressoRequest;
import verzelEvents.dto.response.ValidacaoResponse;
import verzelEvents.entity.*;
import verzelEvents.repository.IngressoRepository;
import verzelEvents.repository.UsuarioRepository;
import verzelEvents.repository.ValidacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortariaServiceTest {

    @Mock private IngressoRepository ingressoRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ValidacaoRepository validacaoRepository;
    @Mock private QrCodeService qrCodeService;

    @InjectMocks private PortariaService portariaService;

    private Usuario portaria;
    private Ingresso ingresso;
    private UUID reservaId;
    private UUID eventoId;

    @BeforeEach
    void setUp() {
        reservaId = UUID.randomUUID();
        eventoId = UUID.randomUUID();

        portaria = Usuario.builder().id(UUID.randomUUID()).email("portaria@verzel.com").nome("Porteiro").build();

        Evento evento = Evento.builder().id(eventoId).titulo("Matrix").build();
        Reserva reserva = Reserva.builder().id(reservaId).evento(evento).build();

        ingresso = Ingresso.builder()
                .id(UUID.randomUUID())
                .reserva(reserva)
                .status(IngressoStatus.EMITIDO)
                .qrHash("hashValido123")
                .build();
    }

    @Test
    @DisplayName("Deve permitir entrada no primeiro acesso (VALIDO)")
    void deveValidarIngressoComSucesso() {
        ValidarIngressoRequest request = new ValidarIngressoRequest();
        request.setCodigo(reservaId + ":hashValido123");
        request.setEventoId(eventoId);

        when(usuarioRepository.findByEmail("portaria@verzel.com")).thenReturn(Optional.of(portaria));
        when(ingressoRepository.findByReservaId(reservaId)).thenReturn(Optional.of(ingresso));
        when(qrCodeService.isValid(reservaId, eventoId, "hashValido123")).thenReturn(true);

        ValidacaoResponse response = portariaService.validateTicket(request, "portaria@verzel.com");

        assertEquals("VALIDO", response.getResultado());
        assertEquals(IngressoStatus.VALIDADO, ingresso.getStatus());
        verify(validacaoRepository, times(1)).save(any(Validacao.class));
    }

    @Test
    @DisplayName("Deve barrar tentativa de reuso (JA_UTILIZADO)")
    void deveBarrarIngressoJaUtilizado() {
        ingresso.setStatus(IngressoStatus.VALIDADO);

        ValidarIngressoRequest request = new ValidarIngressoRequest();
        request.setCodigo(reservaId + ":hashValido123");
        request.setEventoId(eventoId);

        when(usuarioRepository.findByEmail("portaria@verzel.com")).thenReturn(Optional.of(portaria));
        when(ingressoRepository.findByReservaId(reservaId)).thenReturn(Optional.of(ingresso));
        when(qrCodeService.isValid(reservaId, eventoId, "hashValido123")).thenReturn(true);

        ValidacaoResponse response = portariaService.validateTicket(request, "portaria@verzel.com");

        assertEquals("JA_UTILIZADO", response.getResultado());
        verify(ingressoRepository, never()).save(ingresso);
    }
}