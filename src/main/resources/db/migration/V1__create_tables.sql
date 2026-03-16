CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(120) NOT NULL,
    nome VARCHAR(120) NOT NULL,
    senha VARCHAR(120) NOT NULL,
    telefone VARCHAR(120) NOT NULL,
    CONSTRAINT uk_usuario_email UNIQUE (email)
) ENGINE=InnoDB;

CREATE TABLE role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    CONSTRAINT uk_role_nome UNIQUE (nome)
) ENGINE=InnoDB;

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB;

CREATE TABLE categoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    CONSTRAINT uk_categoria_nome UNIQUE (nome)
) ENGINE=InnoDB;

CREATE TABLE produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    marca VARCHAR(120) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    preco_unitario DECIMAL(12,2) NOT NULL,
    quantidade INT NOT NULL,
    categoria_id BIGINT,
    CONSTRAINT uk_produto_nome UNIQUE (nome)
) ENGINE=InnoDB;

CREATE TABLE imagem (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    file_type VARCHAR(255) NOT NULL,
    storage_key VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE carrinho (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT uk_carrinho_usuario UNIQUE (usuario_id)
) ENGINE=InnoDB;

CREATE TABLE carrinho_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrinho_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    CONSTRAINT uk_carrinho_produto UNIQUE (carrinho_id, produto_id)
) ENGINE=InnoDB;

CREATE TABLE pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    data_pedido DATE NOT NULL,
    preco_venda_total DECIMAL(12,2) NOT NULL,
    status ENUM ('PENDENTE','PROCESSANDO','ENVIADO','ENTREGUE','CANCELADO') NOT NULL
) ENGINE=InnoDB;

CREATE TABLE pedido_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_venda_unitario DECIMAL(12,2) NOT NULL
) ENGINE=InnoDB;

-- FOREIGN KEYS

ALTER TABLE carrinho
ADD CONSTRAINT fk_carrinho_usuario
FOREIGN KEY (usuario_id) REFERENCES usuario(id);

ALTER TABLE carrinho_item
ADD CONSTRAINT fk_carrinho_item_carrinho
FOREIGN KEY (carrinho_id) REFERENCES carrinho(id);

ALTER TABLE carrinho_item
ADD CONSTRAINT fk_carrinho_item_produto
FOREIGN KEY (produto_id) REFERENCES produto(id);

ALTER TABLE produto
ADD CONSTRAINT fk_produto_categoria
FOREIGN KEY (categoria_id) REFERENCES categoria(id);

ALTER TABLE imagem
ADD CONSTRAINT fk_imagem_produto
FOREIGN KEY (produto_id) REFERENCES produto(id);

ALTER TABLE pedido
ADD CONSTRAINT fk_pedido_usuario
FOREIGN KEY (usuario_id) REFERENCES usuario(id);

ALTER TABLE pedido_item
ADD CONSTRAINT fk_pedido_item_pedido
FOREIGN KEY (pedido_id) REFERENCES pedido(id);

ALTER TABLE pedido_item
ADD CONSTRAINT fk_pedido_item_produto
FOREIGN KEY (produto_id) REFERENCES produto(id);

ALTER TABLE user_roles
ADD CONSTRAINT fk_user_roles_user
FOREIGN KEY (user_id) REFERENCES usuario(id);

ALTER TABLE user_roles
ADD CONSTRAINT fk_user_roles_role
FOREIGN KEY (role_id) REFERENCES role(id);