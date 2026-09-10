
/**
 * Representa os papéis (roles) de acesso e permissões dos usuários no sistema.
 */
public enum RoleEnum {

    /** Usuário padrão que pesquisa eventos, realiza reservas e adquire ingressos. */
    CLIENTE,

    /** Usuário administrador/criador de eventos, capaz de publicar no catálogo e cadastrar staff. */
    ORGANIZADOR,

    /** Usuário operacional restrito à tela de validação de ingressos (leitura de QR Code) na entrada do evento. */
    PORTARIA
}
