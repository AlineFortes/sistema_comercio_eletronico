package com.ecommerce.view;

import com.ecommerce.controller.ProdutoController;
import com.ecommerce.enums.CategoriaProduto;
import com.ecommerce.exception.ProdutoNaoEncontradoException;
import com.ecommerce.exception.ValidacaoException;
import com.ecommerce.model.Produto;
import com.ecommerce.model.Usuario;
import com.ecommerce.util.FormatoUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Tela de administracao de produtos (apenas admin).
 */
public class TelaAdmin extends JPanel {

    private ProdutoController produtoController;
    private JTable tabela;
    private DefaultTableModel modelo;
    private List<Produto> produtos;

    public TelaAdmin(Usuario usuario) {
        this.produtoController = new ProdutoController();
        inicializar();
    }

    private void inicializar() {
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Gerenciamento de Produtos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Preco", "Categoria", "Estoque", "Ativo"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout());

        JButton btnNovo = new JButton("Novo Produto");
        btnNovo.addActionListener(e -> novoProduto());
        painelBotoes.add(btnNovo);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarProduto());
        painelBotoes.add(btnEditar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirProduto());
        painelBotoes.add(btnExcluir);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarProdutos());
        painelBotoes.add(btnAtualizar);

        add(painelBotoes, BorderLayout.SOUTH);

        carregarProdutos();
    }

    private void carregarProdutos() {
        modelo.setRowCount(0);
        try {
            produtos = produtoController.listarTodos();
            for (Produto p : produtos) {
                modelo.addRow(new Object[]{
                    p.getId(), p.getNome(), FormatoUtil.formatarMoeda(p.getPreco()),
                    p.getCategoria(), p.getQuantidadeEstoque(), p.isAtivo() ? "Sim" : "Nao"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void novoProduto() {
        JTextField txtNome = new JTextField();
        JTextField txtDesc = new JTextField();
        JTextField txtPreco = new JTextField();
        JComboBox<CategoriaProduto> cmbCat = new JComboBox<>(CategoriaProduto.values());
        JTextField txtEstoque = new JTextField();

        Object[] message = {
            "Nome:", txtNome, "Descricao:", txtDesc, "Preco:", txtPreco,
            "Categoria:", cmbCat, "Estoque:", txtEstoque
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Novo Produto", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double preco = Double.parseDouble(txtPreco.getText());
                int estoque = Integer.parseInt(txtEstoque.getText());
                produtoController.cadastrar(txtNome.getText(), txtDesc.getText(), 
                    preco, (CategoriaProduto)cmbCat.getSelectedItem(), estoque);
                carregarProdutos();
                JOptionPane.showMessageDialog(this, "Produto cadastrado!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarProduto() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto!");
            return;
        }
        Produto p = produtos.get(linha);

        JTextField txtNome = new JTextField(p.getNome());
        JTextField txtDesc = new JTextField(p.getDescricao());
        JTextField txtPreco = new JTextField(String.valueOf(p.getPreco()));
        JComboBox<CategoriaProduto> cmbCat = new JComboBox<>(CategoriaProduto.values());
        cmbCat.setSelectedItem(p.getCategoria());
        JTextField txtEstoque = new JTextField(String.valueOf(p.getQuantidadeEstoque()));
        JCheckBox chkAtivo = new JCheckBox("Ativo", p.isAtivo());

        Object[] message = {
            "Nome:", txtNome, "Descricao:", txtDesc, "Preco:", txtPreco,
            "Categoria:", cmbCat, "Estoque:", txtEstoque, chkAtivo
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Editar Produto", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double preco = Double.parseDouble(txtPreco.getText());
                int estoque = Integer.parseInt(txtEstoque.getText());
                produtoController.atualizar(p.getId(), txtNome.getText(), txtDesc.getText(),
                    preco, (CategoriaProduto)cmbCat.getSelectedItem(), estoque, chkAtivo.isSelected());
                carregarProdutos();
                JOptionPane.showMessageDialog(this, "Produto atualizado!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluirProduto() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir este produto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                produtoController.excluir(produtos.get(linha).getId());
                carregarProdutos();
                JOptionPane.showMessageDialog(this, "Produto excluido!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}