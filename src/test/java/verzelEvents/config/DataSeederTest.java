package verzelEvents.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import verzelEvents.entity.Evento;
import verzelEvents.repository.AssentoRepository;
import verzelEvents.repository.EventoRepository;
import verzelEvents.repository.UsuarioRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private AssentoRepository assentoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataSeeder dataSeeder;

    @Test
    @DisplayName("Deve popular o banco de dados quando ele estiver vazio")
    void devePopularBancoQuandoVazio() {
        when(usuarioRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(eventoRepository.save(any(Evento.class))).thenAnswer(i -> i.getArgument(0));

        dataSeeder.run();

        verify(passwordEncoder, times(1)).encode("123456");
        verify(usuarioRepository, times(1)).saveAll(anyList());
        verify(eventoRepository, times(1)).save(any(Evento.class));
        verify(assentoRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Não deve popular o banco quando já existirem usuários")
    void naoDevePopularQuandoBancoJaPossuiDados() {
        when(usuarioRepository.count()).thenReturn(4L);

        dataSeeder.run();

        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).saveAll(anyList());
        verify(eventoRepository, never()).save(any(Evento.class));
        verify(assentoRepository, never()).saveAll(anyList());
    }
}