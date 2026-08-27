package verzelEvents.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class QrCodeService {

    @Value("${app.qr.secret}")
    private String qrSecret;

    public String generateHash(UUID reservaId, UUID eventoId) {
        try {
            String payload = reservaId + ":" + eventoId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(qrSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash do QR code", e);
        }
    }

    public boolean isValid(UUID ingressoId, UUID eventoId, String hashRecebido) {
        String hashEsperado = generateHash(ingressoId, eventoId);
        return hashEsperado.equals(hashRecebido);
    }
}