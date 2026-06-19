package com.ecommerce.view;

import com.ecommerce.model.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Tela Principal do sistema.
 * Interface grafica responsiva usando Java Swing.
 */
public class TelaPrincipal extends JFrame {

    private Usuario usuarioLogado;
    private JTabbedPane abas;

    public TelaPrincipal(Usuario usuario) {
        this.usuarioLogado = usuario;
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("Sistema de Comercio Eletronico - " + usuarioLogado.getNome());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    private void inicializarComponentes() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));
        menuArquivo.add(itemSair);
        menuBar.add(menuArquivo);

        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> mostrarSobre());
        menuAjuda.add(itemSobre);
        menuBar.add(menuAjuda);

        setJMenuBar(menuBar);

        JPanel painelBoasVindas = new JPanel(new BorderLayout());
        JLabel lblBoasVindas = new JLabel(
            "Bem-vindo, " + usuarioLogado.getNome() + "!", 
            SwingConstants.CENTER
        );
        lblBoasVindas.setFont(new Font("Arial", Font.BOLD, 24));
        painelBoasVindas.add(lblBoasVindas, BorderLayout.CENTER);

        abas = new JTabbedPane();
        abas.addTab("Inicio", painelBoasVindas);
        abas.addTab("Produtos", new TelaProdutos(usuarioLogado));
        abas.addTab("Carrinho", new TelaCarrinho(usuarioLogado));
        abas.addTab("Meus Pedidos", new TelaPedidos(usuarioLogado));

        if (usuarioLogado.isAdministrador()) {
            abas.addTab("Gerenciar Produtos", new TelaAdmin(usuarioLogado));
            abas.addTab("Relatorios", new TelaRelatorios());
        }

        add(abas);
    }

    private void mostrarSobre() {
        JOptionPane.showMessageDialog(this,
            "Sistema de Comercio Eletronico\n" +
            "Versao 1.0.0\n" +
            "Desenvolvido para Programacao Orientada a Objetos\n" +
            "Universidade do Mindelo - EISC 2o Ano",
            "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }
}