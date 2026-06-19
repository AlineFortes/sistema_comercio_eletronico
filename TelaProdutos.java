package com.ecommerce.view;

import com.ecommerce.controller.CarrinhoController;
import com.ecommerce.controller.ProdutoController;
import com.ecommerce.enums.CategoriaProduto;
import com.ecommerce.exception.EstoqueInsuficienteException;
import com.ecommerce.exception.ValidacaoException;
import com.ecommerce.model.Produto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.CarrinhoService;
import com.ecommerce.util.FormatoUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Tela de listagem de produtos para compra.
 */
public class TelaProdutos extends JPanel {

    private Usuario usuario;
    private ProdutoController produtoController;
    private CarrinhoController carrinhoController;
    private JTable tabelaProdutos;
    private DefaultTableModel modelo;
    private JComboBox<CategoriaProduto> cmbCategoria;
    private JTextField txtBusca;
    private List<Produto> produtosAtuais;

    public TelaProdutos(Usuario usuario) {
        this.usuario = usuario;
        this.produtoController = new ProdutoController();
        this.carrinhoController = new CarrinhoController(new CarrinhoService());
        inicializar();
    }

    private void inicializar() {
        setLayout(new BorderLayout());

        JPanel painelFiltros = new JPanel(new FlowLayout());
        painelFiltros.add(new JLabel("Categoria:"));

        cmbCategoria = new JComboBox<>();
        cmbCategoria.addItem(null);
        for (CategoriaProduto cat : CategoriaProduto.values()) cmbCategoria.addItem(cat);
        cmbCategoria.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) setText("Todas");
                else setText(((CategoriaProduto)value).getNome());
                return this;
            }
        });
        cmbCategoria.addActionListener(e -> carregarProdutos());
        painelFiltros.add(cmbCategoria);

        txtBusca = new JTextField(20);
        painelFiltros.add(new JLabel("Buscar:"));
        painelFiltros.add(txtBusca);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarPorNome());
        painelFiltros.add(btnBuscar);

        JButton btnTodos = new JButton("Todos");
        btnTodos.addActionListener(e -> carregarProdutos());
        painelFiltros.add(btnTodos);

        add(painelFiltros, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Descricao", "Preco", "Categoria", "Estoque"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaProdutos = new JTable(modelo);
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);

        JPanel painelAcoes = new JPanel(new FlowLayout());
        JButton btnAdicionar = new JButton("Adicionar ao Carrinho");
        btnAdicionar.addActionListener(e -> adicionarAoCarrinho());
        painelAcoes.add(btnAdicionar);

        JButton btnDetalhes = new JButton("Ver Detalhes");
        btnDetalhes.addActionListener(e -> verDetalhes());
        painelAcoes.add(btnDetalhes);

        add(painelAcoes, BorderLayout.SOUTH);

        carregarProdutos();
    }

    private void carregarProdutos() {
        modelo.setRowCount(0);
        try {
            CategoriaProduto cat = (CategoriaProduto) cmbCategoria.getSelectedItem();
            if (cat == null) produtosAtuais = produtoController.listarTodos();
            else produtosAtuais = produtoController.buscarPorCategoria(cat);

            for (Produto p : produtosAtuais) {
                modelo.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getDescricao(), 
                    FormatoUtil.formatarMoeda(p.getPreco()),
                    p.getCategoria(), p.getQuantidadeEstoque()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void buscarPorNome() {
        String nome = txtBusca.getText().trim();
        if (nome.isEmpty()) { carregarProdutos(); return; }
        modelo.setRowCount(0);
        try {
            produtosAtuais = produtoController.buscarPorNome(nome);
            for (Produto p : produtosAtuais) {
                modelo.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getDescricao(),
                    FormatoUtil.formatarMoeda(p.getPreco()),
                    p.getCategoria(), p.getQuantidadeEstoque()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage());
        }
    }

    private void adicionarAoCarrinho() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto!");
            return;
        }
        Produto produto = produtosAtuais.get(linha);
        String input = JOptionPane.showInputDialog(this, 
            "Quantidade de \"" + produto.getNome() + "\" (disponivel: " + 
            produto.getQuantidadeEstoque() + "):", "1");
        if (input == null) return;
        try {
            int qtd = Integer.parseInt(input);
            carrinhoController.adicionarProduto(usuario, produto, qtd);
            JOptionPane.showMessageDialog(this, 
                qtd + "x " + produto.getNome() + " adicionado ao carrinho!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade invalida!");
        } catch (ValidacaoException | EstoqueInsuficienteException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDetalhes() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha < 0) return;
        Produto p = produtosAtuais.get(linha);
        JOptionPane.showMessageDialog(this,
            "Nome: " + p.getNome() + "\n" +
            "Descricao: " + p.getDescricao() + "\n" +
            "Preco: " + FormatoUtil.formatarMoeda(p.getPreco()) + "\n" +
            "Categoria: " + p.getCategoria().getNome() + "\n" +
            "Estoque: " + p.getQuantidadeEstoque(),
            "Detalhes do Produto", JOptionPane.INFORMATION_MESSAGE);
    }
}