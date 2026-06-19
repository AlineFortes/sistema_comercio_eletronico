package com.ecommerce.view;

import com.ecommerce.controller.PedidoController;
import com.ecommerce.model.Pedido;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.CarrinhoService;
import com.ecommerce.util.FormatoUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Tela de visualizacao de pedidos.
 */
public class TelaPedidos extends JPanel {

    private Usuario usuario;
    private PedidoController pedidoController;
    private JTable tabelaPedidos;
    private DefaultTableModel modelo;

    public TelaPedidos(Usuario usuario) {
        this.usuario = usuario;
        this.pedidoController = new PedidoController(new CarrinhoService());
        inicializar();
    }

    private void inicializar() {
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Meus Pedidos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Data", "Total", "Status", "Itens"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaPedidos = new JTable(modelo);
        add(new JScrollPane(tabelaPedidos), BorderLayout.CENTER);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarPedidos());
        add(btnAtualizar, BorderLayout.SOUTH);

        carregarPedidos();
    }

    private void carregarPedidos() {
        modelo.setRowCount(0);
        try {
            List<Pedido> pedidos = pedidoController.listarPorUsuario(usuario.getId());
            for (Pedido p : pedidos) {
                modelo.addRow(new Object[]{
                    p.getId(),
                    FormatoUtil.formatarData(p.getDataPedido()),
                    FormatoUtil.formatarMoeda(p.getValorTotal()),
                    p.getStatus().getDescricao(),
                    p.getQuantidadeTotal()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
}