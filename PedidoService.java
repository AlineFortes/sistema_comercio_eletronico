package com.ecommerce.service;

import com.ecommerce.dao.PedidoDAO;
import com.ecommerce.dao.ProdutoDAO;
import com.ecommerce.enums.StatusPedido;
import com.ecommerce.exception.CarrinhoVazioException;
import com.ecommerce.exception.EstoqueInsuficienteException;
import com.ecommerce.exception.ValidacaoException;
import com.ecommerce.model.Carrinho;
import com.ecommerce.model.ItemCarrinho;
import com.ecommerce.model.Pedido;
import com.ecommerce.model.Usuario;

import java.sql.SQLException;
import java.util.List;

/**
 * Service para Pedido.
 */
public class PedidoService {

    private PedidoDAO pedidoDAO;
    private ProdutoDAO produtoDAO;
    private CarrinhoService carrinhoService;

    public PedidoService(CarrinhoService carrinhoService) {
        this.pedidoDAO = new PedidoDAO();
        this.produtoDAO = new ProdutoDAO();
        this.carrinhoService = carrinhoService;
    }

    public Pedido finalizarCompra(Usuario usuario, String enderecoEntrega) 
            throws CarrinhoVazioException, EstoqueInsuficienteException, 
                   ValidacaoException, SQLException {

        Carrinho carrinho = carrinhoService.getCarrinho(usuario);
        carrinho.validarParaFinalizar();

        if (enderecoEntrega == null || enderecoEntrega.trim().isEmpty()) {
            throw new ValidacaoException("Endereco de entrega obrigatorio.");
        }

        Pedido pedido = new Pedido(0, usuario, carrinho, enderecoEntrega);

        for (ItemCarrinho item : carrinho.getItens()) {
            item.getProduto().reduzirEstoque(item.getQuantidade());
            produtoDAO.atualizar(item.getProduto());
        }

        pedidoDAO.inserir(pedido);
        carrinhoService.limparCarrinho(usuario);

        return pedido;
    }

    public void atualizarStatus(int pedidoId, StatusPedido status) throws SQLException {
        pedidoDAO.atualizarStatus(pedidoId, status);
    }

    public List<Pedido> listarPorUsuario(int usuarioId) throws SQLException {
        return pedidoDAO.listarPorUsuario(usuarioId);
    }

    public List<Pedido> listarTodos() throws SQLException {
        return pedidoDAO.listarTodos();
    }
}