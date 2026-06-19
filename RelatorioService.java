package com.ecommerce.service;

import com.ecommerce.dao.PedidoDAO;
import com.ecommerce.dao.ProdutoDAO;
import com.ecommerce.dao.UsuarioDAO;
import com.ecommerce.model.ItemPedido;
import com.ecommerce.model.Pedido;
import com.ecommerce.model.Produto;
import com.ecommerce.model.Usuario;

import java.sql.SQLException;
import java.util.*;

/**
 * Service para geracao de relatorios.
 */
public class RelatorioService {

    private PedidoDAO pedidoDAO;
    private ProdutoDAO produtoDAO;
    private UsuarioDAO usuarioDAO;

    public RelatorioService() {
        this.pedidoDAO = new PedidoDAO();
        this.produtoDAO = new ProdutoDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    public Map<String, Object> relatorioVendas() throws SQLException {
        Map<String, Object> relatorio = new HashMap<>();
        List<Pedido> pedidos = pedidoDAO.listarTodos();

        double totalVendas = 0;
        int totalPedidos = pedidos.size();
        int totalItens = 0;

        for (Pedido p : pedidos) {
            totalVendas += p.getValorTotal();
            totalItens += p.getQuantidadeTotal();
        }

        relatorio.put("totalVendas", totalVendas);
        relatorio.put("totalPedidos", totalPedidos);
        relatorio.put("totalItens", totalItens);
        relatorio.put("ticketMedio", totalPedidos > 0 ? totalVendas / totalPedidos : 0);
        relatorio.put("pedidos", pedidos);

        return relatorio;
    }

    public List<Map<String, Object>> produtosMaisVendidos() throws SQLException {
        Map<Integer, Integer> contagem = new HashMap<>();
        Map<Integer, Double> receita = new HashMap<>();

        List<Pedido> pedidos = pedidoDAO.listarTodos();
        for (Pedido p : pedidos) {
            for (ItemPedido item : p.getItens()) {
                contagem.merge(item.getProdutoId(), item.getQuantidade(), Integer::sum);
                receita.merge(item.getProdutoId(), item.getSubtotal(), Double::sum);
            }
        }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : contagem.entrySet()) {
            Map<String, Object> prod = new HashMap<>();
            Produto p = produtoDAO.buscarPorId(entry.getKey());
            prod.put("produto", p);
            prod.put("quantidadeVendida", entry.getValue());
            prod.put("receita", receita.getOrDefault(entry.getKey(), 0.0));
            resultado.add(prod);
        }

        resultado.sort((a, b) -> ((Integer)b.get("quantidadeVendida")).compareTo((Integer)a.get("quantidadeVendida")));
        return resultado;
    }

    public List<Map<String, Object>> usuariosMaisAtivos() throws SQLException {
        Map<Integer, Integer> contagem = new HashMap<>();
        Map<Integer, Double> gasto = new HashMap<>();

        List<Pedido> pedidos = pedidoDAO.listarTodos();
        for (Pedido p : pedidos) {
            int uid = p.getUsuario().getId();
            contagem.merge(uid, 1, Integer::sum);
            gasto.merge(uid, p.getValorTotal(), Double::sum);
        }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : contagem.entrySet()) {
            Map<String, Object> user = new HashMap<>();
            Usuario u = usuarioDAO.buscarPorId(entry.getKey());
            user.put("usuario", u);
            user.put("totalPedidos", entry.getValue());
            user.put("totalGasto", gasto.getOrDefault(entry.getKey(), 0.0));
            resultado.add(user);
        }

        resultado.sort((a, b) -> ((Integer)b.get("totalPedidos")).compareTo((Integer)a.get("totalPedidos")));
        return resultado;
    }

    public List<Produto> produtosEstoqueBaixo(int limite) throws SQLException {
        List<Produto> todos = produtoDAO.listarTodos();
        List<Produto> baixo = new ArrayList<>();
        for (Produto p : todos) {
            if (p.getQuantidadeEstoque() <= limite) baixo.add(p);
        }
        return baixo;
    }
}