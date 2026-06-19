package com.ecommerce.service;

import com.ecommerce.dao.ProdutoDAO;
import com.ecommerce.enums.CategoriaProduto;
import com.ecommerce.exception.ProdutoNaoEncontradoException;
import com.ecommerce.exception.ValidacaoException;
import com.ecommerce.model.Produto;

import java.sql.SQLException;
import java.util.List;

/**
 * Service para Produto.
 * Contem a logica de negocio e validacoes.
 */
public class ProdutoService {

    private ProdutoDAO produtoDAO;

    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO();
    }

    public void cadastrar(Produto produto) throws ValidacaoException, SQLException {
        if (produto == null) throw new ValidacaoException("Produto nao pode ser nulo.");
        produtoDAO.inserir(produto);
    }

    public void atualizar(Produto produto) throws ValidacaoException, ProdutoNaoEncontradoException, SQLException {
        if (produto == null || produto.getId() <= 0) throw new ValidacaoException("Produto invalido.");
        if (produtoDAO.buscarPorId(produto.getId()) == null) throw new ProdutoNaoEncontradoException(produto.getId());
        produtoDAO.atualizar(produto);
    }

    public void excluir(int id) throws ProdutoNaoEncontradoException, SQLException {
        if (produtoDAO.buscarPorId(id) == null) throw new ProdutoNaoEncontradoException(id);
        produtoDAO.excluir(id);
    }

    public Produto buscarPorId(int id) throws ProdutoNaoEncontradoException, SQLException {
        Produto p = produtoDAO.buscarPorId(id);
        if (p == null) throw new ProdutoNaoEncontradoException(id);
        return p;
    }

    public List<Produto> listarTodos() throws SQLException {
        return produtoDAO.listarTodos();
    }

    public List<Produto> buscarPorNome(String nome) throws SQLException {
        return produtoDAO.buscarPorNome(nome);
    }

    public List<Produto> buscarPorCategoria(CategoriaProduto categoria) throws SQLException {
        return produtoDAO.buscarPorCategoria(categoria);
    }
}