package dev.charles.gerador_etiquetas.view;

import dev.charles.gerador_etiquetas.bo.LogAcaoBO;
import dev.charles.gerador_etiquetas.controller.UsuarioController;
import dev.charles.gerador_etiquetas.model.Usuario;
import dev.charles.gerador_etiquetas.util.SessaoUsuario;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private UsuarioController usuarioController;

    public TelaLogin() {
        this.usuarioController = new UsuarioController();

        setTitle("Login - Gerador de Etiquetas");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        criarComponentes();
    }

    private void criarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Gerador de Etiquetas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel painelCampos = new JPanel(new GridLayout(4, 1, 5, 5));

        txtEmail = new JTextField("");
        txtSenha = new JPasswordField("");

        painelCampos.add(new JLabel("E-mail:"));
        painelCampos.add(txtEmail);
        painelCampos.add(new JLabel("Senha:"));
        painelCampos.add(txtSenha);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(e -> entrar());

        painelPrincipal.add(titulo, BorderLayout.NORTH);
        painelPrincipal.add(painelCampos, BorderLayout.CENTER);
        painelPrincipal.add(btnEntrar, BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private void entrar() {
        try {
            String email = txtEmail.getText().trim();
            String senha = new String(txtSenha.getPassword());

            Usuario usuarioLogado = usuarioController.autenticarUsuario(email, senha);

            // Inicia a sessão do usuário logado
            SessaoUsuario.iniciar(usuarioLogado);

            // Registra o log de login
            LogAcaoBO logAcaoBO = new LogAcaoBO();
            logAcaoBO.registrar(
                    "LOGIN",
                    "USUARIO",
                    usuarioLogado.getId(),
                    "Usuário entrou no sistema: " + usuarioLogado.getLogin()
            );

            TelaPrincipal telaPrincipal = new TelaPrincipal(usuarioLogado);
            telaPrincipal.setVisible(true);

            dispose();

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Erro no login",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}