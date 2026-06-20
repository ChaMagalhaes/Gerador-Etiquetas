package dev.charles.gerador_etiquetas.view;

import dev.charles.gerador_etiquetas.controller.EtiquetaController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelaFormularioEtiqueta extends JDialog {

    private EtiquetaController etiquetaController;
    private Runnable aoSalvar;

    private JTextField txtDescricao;
    private JTextField txtPrateleira;
    private JTextField txtGrupo;
    private JTextField txtCodigoVenda;
    private JTextArea txtCodigosOriginais;
    private JTextField txtLargura;
    private JTextField txtAltura;

    public TelaFormularioEtiqueta(JFrame telaPai, Runnable aoSalvar) {
        super(telaPai, "Nova Etiqueta", true);

        this.etiquetaController = new EtiquetaController();
        this.aoSalvar = aoSalvar;

        setSize(650, 520);
        setLocationRelativeTo(telaPai);
        setResizable(false);

        criarComponentes();
    }

    private void criarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Cadastro de Etiqueta", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel painelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDescricao = new JTextField();
        txtPrateleira = new JTextField();
        txtGrupo = new JTextField();
        txtCodigoVenda = new JTextField();
        txtCodigosOriginais = new JTextArea(4, 20);
        txtLargura = new JTextField("12");
        txtAltura = new JTextField("6");

        JScrollPane scrollCodigos = new JScrollPane(txtCodigosOriginais);

        adicionarCampoTexto(painelCampos, gbc, 0, "Descrição:", txtDescricao);
        adicionarCampoTexto(painelCampos, gbc, 1, "Prateleira:", txtPrateleira);
        adicionarCampoTexto(painelCampos, gbc, 2, "Grupo:", txtGrupo);
        adicionarCampoTexto(painelCampos, gbc, 3, "Código venda:", txtCodigoVenda);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        painelCampos.add(new JLabel("Códigos originais:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1;
        painelCampos.add(scrollCodigos, gbc);

        adicionarCampoTexto(painelCampos, gbc, 5, "Largura cm:", txtLargura);
        adicionarCampoTexto(painelCampos, gbc, 6, "Altura cm:", txtAltura);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSalvar = new JButton("SALVAR");
        JButton btnCancelar = new JButton("CANCELAR");

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        painelPrincipal.add(titulo, BorderLayout.NORTH);
        painelPrincipal.add(painelCampos, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private void adicionarCampoTexto(JPanel painel, GridBagConstraints gbc, int linha, String label, JTextField campo) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(130, 25));
        painel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.gridy = linha;
        gbc.weightx = 1;
        painel.add(campo, gbc);
    }

    private void salvar() {
        try {
            String descricao = txtDescricao.getText().trim();
            String prateleira = txtPrateleira.getText().trim();
            String grupo = txtGrupo.getText().trim();
            String codigoVenda = txtCodigoVenda.getText().trim();
            String codigosTexto = txtCodigosOriginais.getText().trim();
            double largura = converterNumero(txtLargura.getText());
            double altura = converterNumero(txtAltura.getText());

            etiquetaController.cadastrarEtiqueta(
                    descricao,
                    prateleira,
                    grupo,
                    codigoVenda,
                    converterCodigosOriginais(codigosTexto),
                    largura,
                    altura
            );

            JOptionPane.showMessageDialog(this, "Etiqueta cadastrada com sucesso!");

            if (aoSalvar != null) {
                aoSalvar.run();
            }

            dispose();
        } catch (NumberFormatException e) {
            mostrarAviso("Largura e altura precisam ser números. Exemplo: 12 ou 6,5.");
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao salvar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<String> converterCodigosOriginais(String texto) {
        if (texto == null || texto.isBlank()) {
            return new ArrayList<>();
        }

        String textoFormatado = texto.replace(";", ",").replace("\n", ",");

        return Arrays.stream(textoFormatado.split(","))
                .map(String::trim)
                .filter(codigo -> !codigo.isBlank())
                .toList();
    }

    private double converterNumero(String texto) {
        return Double.parseDouble(texto.trim().replace(",", "."));
    }

    private void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }
}
