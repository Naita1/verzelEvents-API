package verzelEvents.entity;

/**
 * Representa o ciclo de vida e o estado atual de um ingresso emitido.
 */
public enum IngressoStatus {

    /** Ingresso gerado após a confirmação do pagamento, aguardando uso. */
    EMITIDO,

    /** Ingresso que já foi apresentado e lido com sucesso na portaria do evento. */
    VALIDADO,

    /** Ingresso cancelado (ex: estorno de pagamento ou fraude). Não permite entrada. */
    CANCELADO
}