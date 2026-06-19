package com.ecommerce.controller;

import com.ecommerce.enums.CategoriaProduto;
import com.ecommerce.exception.ProdutoNaoEncontradoException;
import com.ecommerce.exception.ValidacaoException;
import com.ecommerce.model.Produto;
import com.ecommerce.service.ProdutoService;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller para Produto.
 */
public class ProdutoController {

    private ProdutoService produtoService;

    public ProdutoController() {
        this.produtoService = new ProdutoService();
    }

    public void cadastrar(String nome, String descricao, double preco, 
                          CategoriaProduto categoria, int estoque) 
            throws ValidacaoException, SQLException {
        Produto p = new Produto(0, nome, descricao, preco, categoria, estoque);
        produtoService.cadastrar(p);
    }

    public void atualizar(int id, String nome, String descricao, double preco,
                          CategoriaProduto categoria, int estoque, boolean ativo) 
            throws ValidacaoException, ProdutoNaoEncontradoException, SQLException {
        Produto p = new Produto(id, nome, descricao, preco, categoria, estoque);
        p.setAtivo(ativo);
        produtoService.atualizar(p);
    }

    public void excluir(int id) throws ProdutoNaoEncontradoException, SQLException {
        produtoService.excluir(id);
    }

    public Produto buscarPorId(int id) throws ProdutoNaoEncontradoException, SQLException {
        return produtoService.buscarPorId(id);
    }

    public List<Produto> listarTodos() throws SQLException {
        return produtoService.listarTodos();
    }

    public List<Produto> buscarPorNome(String nome) throws SQLException {
        return produtoService.buscarPorNome(nome);
    }

    public List<Produto> buscarPorCategoria(CategoriaProduto categoria) throws SQLException {
        return produtoService.buscarPorCategoria(categoria);
    }
}