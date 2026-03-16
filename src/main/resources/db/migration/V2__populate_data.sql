-- ROLES
INSERT INTO role (nome) VALUES ('ADMIN');
INSERT INTO role (nome) VALUES ('USER');

-- USUARIOS
-- senha = 123456 (BCrypt)
INSERT INTO usuario (nome, email, senha, telefone) VALUES
('Administrador', 'admin@email.com', '$2a$10$Dow1lFJYxYk2qkF6J8S6VuXrQq6QJzYQh1F2gk6Y9Z3q2g7mV6L2e', '11999999999'),
('Carlos Silva', 'carlos@email.com', '$2a$10$Dow1lFJYxYk2qkF6J8S6VuXrQq6QJzYQh1F2gk6Y9Z3q2g7mV6L2e', '11988888888'),
('Ana Souza', 'ana@email.com', '$2a$10$Dow1lFJYxYk2qkF6J8S6VuXrQq6QJzYQh1F2gk6Y9Z3q2g7mV6L2e', '11977777777'),
('João Lima', 'joao@email.com', '$2a$10$Dow1lFJYxYk2qkF6J8S6VuXrQq6QJzYQh1F2gk6Y9Z3q2g7mV6L2e', '11966666666');

-- USUARIO_ROLES
INSERT INTO user_roles (user_id, role_id) VALUES
(1,1),
(2,2),
(3,2),
(4,2);

-- CATEGORIAS
INSERT INTO categoria (nome) VALUES
('Eletrônicos'),
('Livros'),
('Informática'),
('Acessórios'),
('Games');

-- PRODUTOS (16)
INSERT INTO produto (nome, marca, descricao, preco_unitario, quantidade, categoria_id) VALUES
('Notebook Dell', 'Dell', 'Notebook 16GB RAM', 4500.00, 10, 1),
('Smartphone Galaxy S23', 'Samsung', 'Smartphone 256GB', 3800.00, 12, 1),
('iPhone 14', 'Apple', 'Smartphone 128GB', 5200.00, 6, 1),

('Livro Java Completo', 'Casa do Código', 'Guia avançado Java', 120.00, 40, 2),
('Clean Code', 'Robert C. Martin', 'Boas práticas de programação', 150.00, 25, 2),
('Arquitetura Limpa', 'Robert C. Martin', 'Clean Architecture', 170.00, 20, 2),

('SSD 1TB', 'Kingston', 'SSD NVMe rápido', 650.00, 15, 3),
('Memória RAM 16GB', 'Corsair', 'DDR4 3200MHz', 450.00, 18, 3),
('HD Externo 2TB', 'Seagate', 'HD portátil', 520.00, 14, 3),

('Mouse Gamer', 'Logitech', 'Mouse RGB 16000 DPI', 280.00, 30, 4),
('Teclado Mecânico', 'Redragon', 'Teclado RGB', 350.00, 22, 4),
('Headset Gamer', 'HyperX', 'Headset com microfone', 420.00, 19, 4),

('PlayStation 5', 'Sony', 'Console PS5', 4800.00, 5, 5),
('Controle PS5', 'Sony', 'Controle DualSense', 420.00, 15, 5),
('Xbox Series X', 'Microsoft', 'Console Xbox', 4700.00, 4, 5),
('Controle Xbox', 'Microsoft', 'Controle sem fio', 390.00, 20, 5);

-- CARRINHOS
INSERT INTO carrinho (usuario_id) VALUES
(2),
(3),
(4);

-- ITENS DO CARRINHO
INSERT INTO carrinho_item (carrinho_id, produto_id, quantidade) VALUES
(1,1,1),
(1,10,2),

(2,4,1),
(2,5,1),

(3,13,1),
(3,14,2);

-- PEDIDOS
INSERT INTO pedido (usuario_id, data_pedido, status, preco_venda_total) VALUES
(2, CURRENT_DATE, 'PROCESSANDO', 5060.00),
(3, CURRENT_DATE, 'PROCESSANDO', 270.00),
(4, CURRENT_DATE, 'PROCESSANDO', 5640.00);

-- PEDIDO_ITENS
INSERT INTO pedido_item (pedido_id, produto_id, quantidade, preco_venda_unitario) VALUES
(1,1,1,4500.00),
(1,10,2,280.00),

(2,4,1,120.00),
(2,5,1,150.00),

(3,13,1,4800.00),
(3,14,2,420.00);