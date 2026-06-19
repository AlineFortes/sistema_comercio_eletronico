package com.ecommerce.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsavel pela conexao com o banco de dados SQLite.
 * Aplica o padrao Singleton para garantir uma unica conexao.
 */
public class ConexaoBD {

    private static final String URL = "jdbc:sqlite:ecommerce.db";
    private static Connection conexao;

    private ConexaoBD() {}

    public static synchronized Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            conexao = DriverManager.getConnection(URL);
            conexao.setAutoCommit(false);
            criarTabelas();
        }
        return conexao;
    }

    private static void criarTabelas() {
        String[] tabelas = {
            "CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, email TEXT UNIQUE NOT NULL, senha_hash TEXT NOT NULL, endereco TEXT NOT NULL, telefone TEXT NOT NULL, tipo TEXT NOT NULL DEFAULT 'CLIENTE', ativo INTEGER DEFAULT 1, data_cadastro TEXT DEFAULT CURRENT_TIMESTAMP)",
            "CREATE TABLE IF NOT EXISTS produtos (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, descricao TEXT NOT NULL, preco REAL NOT NULL, categoria TEXT NOT NULL, quantidade_estoque INTEGER NOT NULL DEFAULT 0, ativo INTEGER DEFAULT 1, data_cadastro TEXT DEFAULT CURRENT_TIMESTAMP)",
            "CREATE TABLE IF NOT EXISTS pedidos (id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_id INTEGER NOT NULL, valor_total REAL NOT NULL, data_pedido TEXT DEFAULT CURRENT_TIMESTAMP, status TEXT NOT NULL DEFAULT 'PENDENTE', endereco_entrega TEXT NOT NULL, FOREIGN KEY (usuario_id) REFERENCES usuarios(id))",
            "CREATE TABLE IF NOT EXISTS itens_pedido (id INTEGER PRIMARY KEY AUTOINCREMENT, pedido_id INTEGER NOT NULL, produto_id INTEGER NOT NULL, nome_produto TEXT NOT NULL, preco_unitario REAL NOT NULL, quantidade INTEGER NOT NULL, subtotal REAL NOT NULL, FOREIGN KEY (pedido_id) REFERENCES pedidos(id))"
        };

        try (Statement stmt = conexao.createStatement()) {
            for (String sql : tabelas) {
                stmt.execute(sql);
            }
            conexao.commit();

            stmt.execute("INSERT OR IGNORE INTO usuarios (id, nome, email, senha_hash, endereco, telefone, tipo) VALUES (1, 'Administrador', 'admin@ecommerce.cv', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/X4.VTtYA.qGZvKG6G', 'Mindelo, Sao Vicente', '+2389999999', 'ADMINISTRADOR')");
            conexao.commit();
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexao: " + e.getMessage());
            }
        }
    }
}