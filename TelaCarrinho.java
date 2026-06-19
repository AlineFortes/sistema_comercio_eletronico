package com.ecommerce.view;

import com.ecommerce.controller.CarrinhoController;
import com.ecommerce.controller.PedidoController;
import com.ecommerce.exception.CarrinhoVazioException;
import com.ecommerce.exception.EstoqueInsuficienteException;
import com.ecommerce.exception.ValidacaoException;
import com.ecommerce.model.ItemCarrinho;
import com.ecommerce.model.Produto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.CarrinhoService;
import com.ecommerce.util.FormatoUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

/**
 * Tela do Carrinho de Compras.
 */
public class TelaCarrinho extends JPanel {

    private Usuario usuario;
    private CarrinhoController carrinhoController;
    private PedidoController pedidoController;
    private JTable tabelaItens;
    private DefaultTableModel modelo;
    private JLabel lblTotal;

    public TelaCarrinho(Usuario usuario) {
        this.usuario = usuario;
        CarrinhoService cs = new CarrinhoService();
        this.carrinhoController = new CarrinhoController(cs);
        this.pedidoController = new PedidoController(cs);
        inicializar();
    }

    private void inicializar() {
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Carrinho de Compras", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        String[] colunas = {"Produto", "Preco Unit.", "Qtd", "Subtotal"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaItens = new JTable(modelo);
        add(new JScrollPane(tabelaItens), BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new BorderLayout());
        lblTotal = new JLabel("Total: CVE 0,00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        painelInferior.add(lblTotal, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> atualizarTabela());
        painelBotoes.add(btnAtualizar);

        JButton btnRemover = new JButton("Remover Item");
        btnRemover.addActionListener(e -> removerItem());
        painelBotoes.add(btnRemover);

        JButton btnLimpar = new JButton("Limpar Carrinho");
        btnLimpar.addActionListener(e -> limparCarrinho());
        painelBotoes.add(btnLimpar);

        JButton btnFinalizar = new JButton("Finalizar Compra");
        btnFinalizar.setBackground(new Color(0, 150, 0));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.addActionListener(e -> finalizarCompra());
        painelBotoes.add(btnFinalizar);

        painelInferior.add(painelBotoes, BorderLayout.SOUTH);
        add(painelInferior, BorderLayout.SOUTH);

        atualizarTabela();
    }

    public void atualizarTabela() {
        modelo.setRowCount(0);
        var carrinho = carrinhoController.getCarrinho(usuario);
        for (ItemCarrinho item : carrinho.getItens()) {
            modelo.addRow(new Object[]{
                item.getProduto().getNome(),
                FormatoUtil.formatarMoeda(item.getPrecoUnitario()),
                item.getQuantidade(),
                FormatoUtil.formatarMoeda(item.getSubtotal())
            });
        }
        lblTotal.setText("Total: " + FormatoUtil.formatarMoeda(carrinhoController.getTotal(usuario)));
    }

    private void removerItem() {
        int linha = tabelaItens.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um item!");
            return;
        }
        var carrinho = carrinhoController.getCarrinho(usuario);
        var itens = new java.util.ArrayList<>(carrinho.getItens());
        Produto produto = itens.get(linha).getProduto();
        carrinhoController.removerProduto(usuario, produto);
        atualizarTabela();
    }

    private void limparCarrinho() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Deseja limpar todo o carrinho?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            carrinhoController.limparCarrinho(usuario);
            atualizarTabela();
        }
    }

    private void finalizarCompra() {
        try {
            carrinhoController.validarParaFinalizar(usuario);
            String endereco = JOptionPane.showInputDialog(this, 
                "Confirme o endereco de entrega:", usuario.getEndereco());
            if (endereco == null || endereco.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Endereco obrigatorio!");
                return;
            }
            var pedido = pedidoController.finalizarCompra(usuario, endereco);
            JOptionPane.showMessageDialog(this, 
                "Pedido #" + pedido.getId() + " realizado com sucesso!\n" +
                "Total: " + FormatoUtil.formatarMoeda(pedido.getValorTotal()),
                "Compra Finalizada", JOptionPane.INFORMATION_MESSAGE);
            atualizarTabela();
        } catch (CarrinhoVazioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Carrinho Vazio", JOptionPane.WARNING_MESSAGE);
        } catch (EstoqueInsuficienteException | ValidacaoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}