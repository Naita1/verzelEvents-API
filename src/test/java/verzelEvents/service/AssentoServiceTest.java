package verzelEvents.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import verzelEvents.exception.ResourceNotFoundException;
import verzelEvents.repository.AssentoRepository;
import verzelEvents.repository.EventoRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssentoServiceTest {

    @Mock
    private AssentoRepository assentoRepository;

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private AssentoService assentoService;

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o evento não existir")
    void deveLancarErroQuandoEventoNaoExistir() {
        UUID eventoId = UUID.randomUUID();

        when(eventoRepository.findById(eventoId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> assentoService.listByEvento(eventoId));
    }
}
