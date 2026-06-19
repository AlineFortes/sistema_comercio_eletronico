package com.ecommerce.controller;

import com.ecommerce.enums.StatusPedido;
import com.ecommerce.exception.CarrinhoVazioException;
import com.ecommerce.exception.EstoqueInsuficienteException;
import com.ecommerce.exception.ValidacaoException;
import com.ecommerce.model.Pedido;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.CarrinhoService;
import com.ecommerce.service.PedidoService;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller para Pedido.
 */
public class PedidoController {

    private PedidoService pedidoService;

    public PedidoController(CarrinhoService carrinhoService) {
        this.pedidoService = new PedidoService(carrinhoService);
    }

    public Pedido finalizarCompra(Usuario usuario, String enderecoEntrega) 
            throws CarrinhoVazioException, EstoqueInsuficienteException, 
                   ValidacaoException, SQLException {
        return pedidoService.finalizarCompra(usuario, enderecoEntrega);
    }

    public void atualizarStatus(int pedidoId, StatusPedido status) throws SQLException {
        pedidoService.atualizarStatus(pedidoId, status);
    }

    public List<Pedido> listarPorUsuario(int usuarioId) throws SQLException {
        return pedidoService.listarPorUsuario(usuarioId);
    }

    public List<Pedido> listarTodos() throws SQLException {
        return pedidoService.listarTodos();
    }
}