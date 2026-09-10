package verzelEvents.entity;

/**
 * Representa o estado atual de um assento no mapa do evento.
 */
public enum AssentoStatus {

    /** Assento disponível para uma nova reserva. */
    LIVRE,

    /** Assento temporariamente bloqueado aguardando pagamento. Expira em 5 minutos. */
    RESERVADO,

    /** Assento com pagamento confirmado e ingresso já emitido. */
    VENDIDO
}