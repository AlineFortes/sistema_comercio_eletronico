package com.ecommerce.view;

import com.ecommerce.controller.UsuarioController;
import com.ecommerce.enums.TipoUsuario;
import com.ecommerce.exception.ValidacaoException;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Tela de Cadastro de Usuario.
 */
public class TelaCadastroUsuario extends JFrame {

    private JTextField txtNome, txtEmail, txtEndereco, txtTelefone;
    private JPasswordField txtSenha, txtConfirmarSenha;
    private UsuarioController usuarioController;

    public TelaCadastroUsuario() {
        this.usuarioController = new UsuarioController();
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("Cadastro de Usuario");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row++;
        painel.add(new JLabel("Nome:"), gbc);
        txtNome = new JTextField(20);
        gbc.gridx = 1;
        painel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        painel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        painel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        painel.add(new JLabel("Senha:"), gbc);
        txtSenha = new JPasswordField(20);
        gbc.gridx = 1;
        painel.add(txtSenha, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        painel.add(new JLabel("Confirmar Senha:"), gbc);
        txtConfirmarSenha = new JPasswordField(20);
        gbc.gridx = 1;
        painel.add(txtConfirmarSenha, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        painel.add(new JLabel("Endereco:"), gbc);
        txtEndereco = new JTextField(20);
        gbc.gridx = 1;
        painel.add(txtEndereco, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        painel.add(new JLabel("Telefone:"), gbc);
        txtTelefone = new JTextField(20);
        gbc.gridx = 1;
        painel.add(txtTelefone, gbc);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> cadastrar());
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        painel.add(btnCadastrar, gbc);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        gbc.gridy = row++;
        painel.add(btnCancelar, gbc);

        add(painel);
    }

    private void cadastrar() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());
        String confirmar = new String(txtConfirmarSenha.getPassword());
        String endereco = txtEndereco.getText().trim();
        String telefone = txtTelefone.getText().trim();

        if (!senha.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "As senhas nao coincidem!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            usuarioController.cadastrar(nome, email, senha, endereco, telefone, TipoUsuario.CLIENTE);
            JOptionPane.showMessageDialog(this, "Usuario cadastrado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (ValidacaoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                "Erro de Validacao", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}