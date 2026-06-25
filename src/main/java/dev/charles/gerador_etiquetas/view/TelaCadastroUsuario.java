package dev.charles.gerador_etiquetas.view;

import dev.charles.gerador_etiquetas.controller.UsuarioController;
import dev.charles.gerador_etiquetas.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaCadastroUsuario extends JDialog {

    private final UsuarioController usuarioController;

    private JTextField txtNome;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JTextField txtEmail;
    private JTextField txtTelefone;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaCadastroUsuario(JFrame telaPai) {
        super(telaPai, "Cadastro de Usuários", true);
        this.usuarioController = new UsuarioController();

        setSize(850, 560);
        setLocationRelativeTo(telaPai);
        criarComponentes();
        carregarUsuarios();
    }

    private void criarComponentes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel titulo = new JLabel("Usuários", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField();
        txtLogin = new JTextField();
        txtSenha = new JPasswordField();
        txtEmail = new JTextField();
        txtTelefone = new JTextField();

        adicionarCampo(form, gbc, 0, "Nome:", txtNome);
        adicionarCampo(form, gbc, 1, "Login:", txtLogin);
        adicionarCampo(form, gbc, 2, "Senha:", txtSenha);
        adicionarCampo(form, gbc, 3, "E-mail:", txtEmail);
        adicionarCampo(form, gbc, 4, "Telefone:", txtTelefone);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSalvar = new JButton("Salvar usuário");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnRecarregar = new JButton("Recarregar");

        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limparCampos());
        btnRecarregar.addActionListener(e -> carregarUsuarios());

        botoes.add(btnSalvar);
        botoes.add(btnLimpar);
        botoes.add(btnRecarregar);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(titulo, BorderLayout.NORTH);
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Login", "E-mail", "Telefone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(painel);
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints gbc, int linha, String label, JTextField campo) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;
        painel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.gridy = linha;
        gbc.weightx = 1;
        painel.add(campo, gbc);
    }

    private void salvar() {
        try {
            String senha = new String(txtSenha.getPassword());
            usuarioController.cadastrarUsuario(
                    txtNome.getText(),
                    txtLogin.getText(),
                    senha,
                    txtEmail.getText(),
                    txtTelefone.getText()
            );

            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            limparCampos();
            carregarUsuarios();
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void carregarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioController.listarUsuarios();
            modeloTabela.setRowCount(0);

            for (Usuario usuario : usuarios) {
                modeloTabela.addRow(new Object[]{
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getLogin(),
                        usuario.getEmail(),
                        usuario.getTelefone()
                });
            }
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
