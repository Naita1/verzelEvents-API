CREATE TABLE usuario (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE evento (
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL,
    organizador_id UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_evento_organizador FOREIGN KEY (organizador_id) REFERENCES usuario (id)
);

CREATE TABLE assento (
    id UUID PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    evento_id UUID NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_assento_evento FOREIGN KEY (evento_id) REFERENCES evento (id)
);

CREATE TABLE reserva (
    id UUID PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    chave_idempotencia VARCHAR(255) UNIQUE,
    evento_id UUID NOT NULL,
    assento_id UUID NOT NULL,
    cliente_id UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reserva_evento FOREIGN KEY (evento_id) REFERENCES evento (id),
    CONSTRAINT fk_reserva_assento FOREIGN KEY (assento_id) REFERENCES assento (id),
    CONSTRAINT fk_reserva_cliente FOREIGN KEY (cliente_id) REFERENCES usuario (id)
);

CREATE TABLE ingresso (
    id UUID PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    qr_hash VARCHAR(255) NOT NULL,
    reserva_id UUID NOT NULL UNIQUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ingresso_reserva FOREIGN KEY (reserva_id) REFERENCES reserva (id)
);

CREATE TABLE validacao (
    id UUID PRIMARY KEY,
    resultado VARCHAR(50) NOT NULL,
    evento_id UUID,
    ingresso_id UUID,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_validacao_evento FOREIGN KEY (evento_id) REFERENCES evento (id),
    CONSTRAINT fk_validacao_ingresso FOREIGN KEY (ingresso_id) REFERENCES ingresso (id)
);