CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    login VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefone VARCHAR(30)
);

CREATE TABLE prateleira (
    id BIGSERIAL PRIMARY KEY,
    local_prateleira VARCHAR(100) NOT NULL,
    descricao_grupo VARCHAR(150)
);

CREATE TABLE grupo (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(150) NOT NULL
);

CREATE TABLE subgrupo (
    id BIGSERIAL PRIMARY KEY,
    grupo_id BIGINT NOT NULL REFERENCES grupo (id),
    descricao VARCHAR(150) NOT NULL
);

CREATE TABLE fabricante (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    tipo VARCHAR(100),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE etiqueta (
    id BIGSERIAL PRIMARY KEY,
    prateleira_id BIGINT REFERENCES prateleira (id),
    grupo_id BIGINT REFERENCES grupo (id),
    subgrupo_id BIGINT REFERENCES subgrupo (id),
    descricao VARCHAR(200) NOT NULL,
    codigo_venda VARCHAR(100) NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    largura_cm DOUBLE PRECISION NOT NULL,
    altura_cm DOUBLE PRECISION NOT NULL
);

CREATE TABLE etiqueta_codigo_original (
    id BIGSERIAL PRIMARY KEY,
    etiqueta_id BIGINT NOT NULL REFERENCES etiqueta (id) ON DELETE CASCADE,
    codigo_original VARCHAR(100) NOT NULL,
    fabricante_id BIGINT REFERENCES fabricante (id)
);

CREATE TABLE log_acao (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuario (id),
    acao VARCHAR(50) NOT NULL,
    entidade VARCHAR(50) NOT NULL,
    entidade_id BIGINT,
    detalhes VARCHAR(500),
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Usuário de teste para permitir login imediato (não há tela de cadastro de usuário no frontend)
INSERT INTO usuario (nome, login, senha, email, telefone)
VALUES ('Administrador', 'admin', '123456', 'admin@teste.com', NULL);
