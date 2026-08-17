package verzelEvents.service;

import verzelEvents.dto.response.IngressoResponse;
import verzelEvents.entity.Ingresso;
import verzelEvents.entity.Usuario;
import verzelEvents.repository.IngressoRepository;
import verzelEvents.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngressoService {

    private final IngressoRepository ingressoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<IngressoResponse> getMyTickets(String clienteEmail) {
        Usuario cliente = usuarioRepository.findByEmail(clienteEmail)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        return ingressoRepository.findByReserva_Cliente_Id(cliente.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public IngressoResponse getSharedTicket(String shareToken) {
        Ingresso ingresso = ingressoRepository.findByShareToken(shareToken)
                .orElseThrow(() -> new IllegalArgumentException("Ingresso não encontrado"));
        return toResponse(ingresso);
    }

    private IngressoResponse toResponse(Ingresso ingresso) {
        return new IngressoResponse(
                ingresso.getId(),
                ingresso.getStatus().name(),
                ingresso.getQrHash(),
                ingresso.getShareToken(),
                ingresso.getReserva().getEvento().getTitulo(),
                ingresso.getReserva().getAssento() != null ? ingresso.getReserva().getAssento().getCodigo() : null,
                ingresso.getReserva().getId() + ":" + ingresso.getQrHash()
        );
    }
}