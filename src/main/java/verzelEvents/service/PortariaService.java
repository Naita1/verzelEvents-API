package verzelEvents.service;

import verzelEvents.dto.request.ValidarIngressoRequest;
import verzelEvents.dto.response.ValidacaoResponse;
import verzelEvents.entity.*;
import verzelEvents.repository.IngressoRepository;
import verzelEvents.repository.UsuarioRepository;
import verzelEvents.repository.ValidacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortariaService {

    private final IngressoRepository ingressoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ValidacaoRepository validacaoRepository;
    private final QrCodeService qrCodeService;

    @Transactional
    public ValidacaoResponse validateTicket(ValidarIngressoRequest request, String portariaEmail) {
        Usuario portaria = usuarioRepository.findByEmail(portariaEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário de portaria não encontrado"));

        String[] partes = request.getCodigo().split(":");
        if (partes.length != 2) {
            return logAndReturn(null, portaria, ResultadoValidacao.INVALIDO, "Código em formato inválido");
        }

        UUID reservaId;
        try {
            reservaId = UUID.fromString(partes[0]);
        } catch (IllegalArgumentException e) {
            return logAndReturn(null, portaria, ResultadoValidacao.INVALIDO, "Código em formato inválido");
        }
        String hashRecebido = partes[1];

        Optional<Ingresso> ingressoOpt = ingressoRepository.findByReservaId(reservaId);
        if (ingressoOpt.isEmpty()) {
            return logAndReturn(null, portaria, ResultadoValidacao.INVALIDO, "Ingresso não encontrado");
        }

        Ingresso ingresso = ingressoOpt.get();
        UUID eventoDoIngresso = ingresso.getReserva().getEvento().getId();

        boolean hashValido = qrCodeService.isValid(reservaId, eventoDoIngresso, hashRecebido);
        if (!hashValido) {
            return logAndReturn(ingresso, portaria, ResultadoValidacao.INVALIDO, "Assinatura do QR não confere — possível fraude");
        }

        if (!eventoDoIngresso.equals(request.getEventoId())) {
            return logAndReturn(ingresso, portaria, ResultadoValidacao.EVENTO_ERRADO, "Este ingresso é de outro evento");
        }

        if (ingresso.getStatus() == IngressoStatus.VALIDADO) {
            return logAndReturn(ingresso, portaria, ResultadoValidacao.JA_UTILIZADO, "Este ingresso já foi validado anteriormente");
        }

        ingresso.setStatus(IngressoStatus.VALIDADO);
        ingressoRepository.save(ingresso);

        return logAndReturn(ingresso, portaria, ResultadoValidacao.VALIDO, "Ingresso válido, acesso liberado");
    }

    public List<String> getValidationHistory(UUID eventoId) {
        return validacaoRepository.findByIngresso_Reserva_Evento_IdOrderByCreatedAtDesc(eventoId).stream()
                .map(v -> v.getCreatedAt() + " — " + v.getResultado() + " — portaria: " + v.getPortaria().getNome())
                .collect(Collectors.toList());
    }

    private ValidacaoResponse logAndReturn(Ingresso ingresso, Usuario portaria, ResultadoValidacao resultado, String mensagem) {
        Validacao validacao = Validacao.builder()
                .ingresso(ingresso)
                .portaria(portaria)
                .resultado(resultado)
                .createdAt(LocalDateTime.now())
                .build();
        validacaoRepository.save(validacao);

        return new ValidacaoResponse(resultado.name(), mensagem);
    }
}