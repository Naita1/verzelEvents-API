package verzelEvents.entity;

/**
 * Representa o estado e o ciclo de vida de uma reserva de assento no sistema.
 */
public enum ReservaStatus {

    /** Reserva criada, assento bloqueado. Aguardando pagamento (expira automaticamente em 5 minutos). */
    PENDENTE,

    /** Pagamento aprovado com sucesso. O ingresso definitivo foi gerado. */
    CONFIRMADA,

    /** Tempo limite de pagamento esgotado. A reserva foi invalidada pelo sistema (cron job) e o assento foi liberado. */
    EXPIRADA,

    /** Reserva cancelada manualmente, ou pagamento estornado. */
    CANCELADA
}