package com.ecommerce.view;

import com.ecommerce.service.RelatorioService;
import com.ecommerce.util.FormatoUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Tela de relatorios (apenas admin).
 */
public class TelaRelatorios extends JPanel {

    private RelatorioService relatorioService;
    private JTextArea txtRelatorio;

    public TelaRelatorios() {
        this.relatorioService = new RelatorioService();
        inicializar();
    }

    private void inicializar() {
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Relatorios do Sistema", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new FlowLayout());

        JButton btnVendas = new JButton("Vendas Totais");
        btnVendas.addActionListener(e -> relatorioVendas());
        painelBotoes.add(btnVendas);

        JButton btnMaisVendidos = new JButton("Produtos Mais Vendidos");
        btnMaisVendidos.addActionListener(e -> produtosMaisVendidos());
        painelBotoes.add(btnMaisVendidos);

        JButton btnUsuariosAtivos = new JButton("Usuarios Mais Ativos");
        btnUsuariosAtivos.addActionListener(e -> usuariosMaisAtivos());
        painelBotoes.add(btnUsuariosAtivos);

        JButton btnEstoque = new JButton("Estoque Baixo");
        btnEstoque.addActionListener(e -> estoqueBaixo());
        painelBotoes.add(btnEstoque);

        add(painelBotoes, BorderLayout.NORTH);

        txtRelatorio = new JTextArea();
        txtRelatorio.setEditable(false);
        txtRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(txtRelatorio), BorderLayout.CENTER);
    }

    private void relatorioVendas() {
        try {
            Map<String, Object> rel = relatorioService.relatorioVendas();
            StringBuilder sb = new StringBuilder();
            sb.append("=== RELATORIO DE VENDAS ===\n\n");
            sb.append("Total de Vendas: ").append(FormatoUtil.formatarMoeda((Double)rel.get("totalVendas"))).append("\n");
            sb.append("Total de Pedidos: ").append(rel.get("totalPedidos")).append("\n");
            sb.append("Total de Itens Vendidos: ").append(rel.get("totalItens")).append("\n");
            sb.append("Ticket Medio: ").append(FormatoUtil.formatarMoeda((Double)rel.get("ticketMedio"))).append("\n");
            txtRelatorio.setText(sb.toString());
        } catch (SQLException e) {
            txtRelatorio.setText("Erro: " + e.getMessage());
        }
    }

    private void produtosMaisVendidos() {
        try {
            List<Map<String, Object>> lista = relatorioService.produtosMaisVendidos();
            StringBuilder sb = new StringBuilder();
            sb.append("=== PRODUTOS MAIS VENDIDOS ===\n\n");
            for (Map<String, Object> item : lista) {
                sb.append(item.get("produto")).append("\n");
                sb.append("  Qtd Vendida: ").append(item.get("quantidadeVendida")).append("\n");
                sb.append("  Receita: ").append(FormatoUtil.formatarMoeda((Double)item.get("receita"))).append("\n\n");
            }
            txtRelatorio.setText(sb.toString());
        } catch (SQLException e) {
            txtRelatorio.setText("Erro: " + e.getMessage());
        }
    }

    private void usuariosMaisAtivos() {
        try {
            List<Map<String, Object>> lista = relatorioService.usuariosMaisAtivos();
            StringBuilder sb = new StringBuilder();
            sb.append("=== USUARIOS MAIS ATIVOS ===\n\n");
            for (Map<String, Object> item : lista) {
                sb.append(item.get("usuario")).append("\n");
                sb.append("  Total Pedidos: ").append(item.get("totalPedidos")).append("\n");
                sb.append("  Total Gasto: ").append(FormatoUtil.formatarMoeda((Double)item.get("totalGasto"))).append("\n\n");
            }
            txtRelatorio.setText(sb.toString());
        } catch (SQLException e) {
            txtRelatorio.setText("Erro: " + e.getMessage());
        }
    }

    private void estoqueBaixo() {
        try {
            var lista = relatorioService.produtosEstoqueBaixo(5);
            StringBuilder sb = new StringBuilder();
            sb.append("=== PRODUTOS COM ESTOQUE BAIXO (<= 5) ===\n\n");
            for (var p : lista) {
                sb.append(p.getNome()).append(" - Estoque: ").append(p.getQuantidadeEstoque()).append("\n");
            }
            txtRelatorio.setText(sb.toString());
        } catch (SQLException e) {
            txtRelatorio.setText("Erro: " + e.getMessage());
        }
    }
}