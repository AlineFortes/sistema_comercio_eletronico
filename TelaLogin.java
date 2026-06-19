package com.ecommerce.view;

import com.ecommerce.controller.UsuarioController;
import com.ecommerce.exception.AutenticacaoException;
import com.ecommerce.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Tela de Login do sistema.
 */
public class TelaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private UsuarioController usuarioController;

    public TelaLogin() {
        this.usuarioController = new UsuarioController();
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("Sistema de Comercio Eletronico - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Login", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        painel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        painel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Senha:"), gbc);
        txtSenha = new JPasswordField(20);
        gbc.gridx = 1;
        painel.add(txtSenha, gbc);

        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(e -> realizarLogin());
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        painel.add(btnLogin, gbc);

        JButton btnCadastro = new JButton("Criar Conta");
        btnCadastro.addActionListener(e -> abrirCadastro());
        gbc.gridy = 4;
        painel.add(btnCadastro, gbc);

        JLabel lblInfo = new JLabel("Admin: admin@ecommerce.cv / admin123", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 10));
        lblInfo.setForeground(Color.GRAY);
        gbc.gridy = 5;
        painel.add(lblInfo, gbc);

        add(painel);
    }

    private void realizarLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Usuario usuario = usuarioController.autenticar(email, senha);
            JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getNome() + "!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            SwingUtilities.invokeLater(() -> {
                TelaPrincipal telaPrincipal = new TelaPrincipal(usuario);
                telaPrincipal.setVisible(true);
                dispose();
            });

        } catch (AutenticacaoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                "Erro de Autenticacao", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro no banco de dados: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirCadastro() {
        TelaCadastroUsuario tela = new TelaCadastroUsuario();
        tela.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new TelaLogin().setVisible(true);
        });
    }
}