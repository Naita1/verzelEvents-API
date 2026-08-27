package verzelEvents.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QrCodeServiceTest {

    private QrCodeService qrCodeService;
    private final String secret = "minha-chave-secreta-super-segura-1234567890";

    @BeforeEach
    void setUp() {
        qrCodeService = new QrCodeService();
        ReflectionTestUtils.setField(qrCodeService, "qrSecret", secret);
    }

    @Test
    @DisplayName("Deve gerar hash valido e confirmar sua autenticidade")
    void deveGerarEValidarHashComSucesso() {
        UUID ingressoId = UUID.randomUUID();
        UUID eventoId = UUID.randomUUID();

        String hash = qrCodeService.generateHash(ingressoId, eventoId);

        assertNotNull(hash);
        assertTrue(qrCodeService.isValid(ingressoId, eventoId, hash));
    }

    @Test
    @DisplayName("Deve rejeitar se o hash for adulterado na portaria (Antifraude)")
    void deveRejeitarHashAdulterado() {
        UUID ingressoId = UUID.randomUUID();
        UUID eventoId = UUID.randomUUID();

        String hashOriginal = qrCodeService.generateHash(ingressoId, eventoId);
        String hashAdulterado = hashOriginal.substring(0, hashOriginal.length() - 1) + "X";

        assertFalse(qrCodeService.isValid(ingressoId, eventoId, hashAdulterado));
    }
}