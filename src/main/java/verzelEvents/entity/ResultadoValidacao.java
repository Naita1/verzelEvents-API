package verzelEvents.entity;

/**
 * Representa o resultado de uma tentativa de validação de ingresso na portaria do evento.
 */
public enum ResultadoValidacao {

    /** O ingresso é autêntico, pertence ao evento correto e ainda não havia sido utilizado. Entrada permitida. */
    VALIDO,

    /** O ingresso é falso, o QR Code foi adulterado ou a reserva/ingresso não existe no banco de dados. */
    INVALIDO,

    /** O ingresso é autêntico, mas já foi validado anteriormente na portaria (tentativa de reuso). */
    JA_UTILIZADO,

    /** O ingresso é autêntico, mas pertence a outro evento diferente do qual a portaria está operando. */
    EVENTO_ERRADO
}