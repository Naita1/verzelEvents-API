package verzelEvents.service;

import verzelEvents.dto.request.PagamentoRequest;
import verzelEvents.dto.response.IngressoResponse;
import verzelEvents.entity.AssentoStatus;
import verzelEvents.entity.Ingresso;
import verzelEvents.entity.IngressoStatus;
import verzelEvents.entity.Reserva;
import verzelEvents.entity.ReservaStatus;
import verzelEvents.exception.*;
import verzelEvents.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final ReservaRepository reservaRepository;
    private final AssentoRepository assentoRepository;
    private final IngressoRepository ingressoRepository;
    private final QrCodeService qrCodeService;

    @Transactional
    public IngressoResponse processPayment(UUID reservaId, PagamentoRequest request, String clienteEmail) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada: " + reservaId));

        if (!reserva.getCliente().getEmail().equals(clienteEmail)) {
            throw new ForbiddenOperationException("Esta reserva não pertence a este cliente");
        }

        if (reserva.getStatus() != ReservaStatus.PENDENTE) {
            throw new InvalidOperationException("Reserva não está pendente de pagamento, status atual: " + reserva.getStatus());
        }

        if (reserva.getExpiresAt().isBefore(LocalDateTime.now())) {
            reserva.setStatus(ReservaStatus.EXPIRADA);
            if (reserva.getAssento() != null) {
                reserva.getAssento().setStatus(AssentoStatus.LIVRE);
                assentoRepository.save(reserva.getAssento());
            }
            reservaRepository.save(reserva);
            throw new ReservaExpiradaException("Esta reserva expirou, faça uma nova reserva");
        }

        if (request.getNumeroCartao().endsWith("0000")) {
            throw new PagamentoRecusadoException("Pagamento recusado pela operadora do cartão");
        }

        reserva.setStatus(ReservaStatus.CONFIRMADA);
        reservaRepository.save(reserva);

        if (reserva.getAssento() != null) {
            reserva.getAssento().setStatus(AssentoStatus.VENDIDO);
            assentoRepository.save(reserva.getAssento());
        }

        String qrHash = qrCodeService.generateHash(reserva.getId(), reserva.getEvento().getId());

        Ingresso ingresso = Ingresso.builder()
                .reserva(reserva)
                .status(IngressoStatus.EMITIDO)
                .shareToken(UUID.randomUUID().toString())
                .qrHash(qrHash)
                .build();
        ingressoRepository.save(ingresso);

        return IngressoResponse.fromEntity(ingresso);
    }
}