-- Schema do Banco de Dados SQLite
-- Sistema de Comercio Eletronico

-- Tabela de Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    senha_hash TEXT NOT NULL,
    endereco TEXT NOT NULL,
    telefone TEXT NOT NULL,
    tipo TEXT NOT NULL DEFAULT 'CLIENTE',
    ativo INTEGER DEFAULT 1,
    data_cadastro TEXT DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Produtos
CREATE TABLE IF NOT EXISTS produtos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    descricao TEXT NOT NULL,
    preco REAL NOT NULL,
    categoria TEXT NOT NULL,
    quantidade_estoque INTEGER NOT NULL DEFAULT 0,
    ativo INTEGER DEFAULT 1,
    data_cadastro TEXT DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Pedidos
CREATE TABLE IF NOT EXISTS pedidos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario_id INTEGER NOT NULL,
    valor_total REAL NOT NULL,
    data_pedido TEXT DEFAULT CURRENT_TIMESTAMP,
    status TEXT NOT NULL DEFAULT 'PENDENTE',
    endereco_entrega TEXT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabela de Itens do Pedido
CREATE TABLE IF NOT EXISTS itens_pedido (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    pedido_id INTEGER NOT NULL,
    produto_id INTEGER NOT NULL,
    nome_produto TEXT NOT NULL,
    preco_unitario REAL NOT NULL,
    quantidade INTEGER NOT NULL,
    subtotal REAL NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id)
);

-- Usuario Administrador Padrao
-- Senha: admin123 (hash BCrypt)
INSERT OR IGNORE INTO usuarios (id, nome, email, senha_hash, endereco, telefone, tipo)
VALUES (1, 'Administrador', 'admin@ecommerce.cv', 
'$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/X4.VTtYA.qGZvKG6G', 
'Mindelo, Sao Vicente', '+2389999999', 'ADMINISTRADOR');
