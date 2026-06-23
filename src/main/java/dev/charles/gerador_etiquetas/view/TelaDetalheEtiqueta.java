package dev.charles.gerador_etiquetas.view;

import dev.charles.gerador_etiquetas.bo.EtiquetaBO;
import dev.charles.gerador_etiquetas.controller.EtiquetaController;
import dev.charles.gerador_etiquetas.model.Etiqueta;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelaDetalheEtiqueta extends JDialog {

    private Etiqueta etiqueta;
    private EtiquetaBO etiquetaBO;
    private Runnable aoAtualizar;

    private JTextField txtId;
    private JTextField txtDescricao;
    private JTextField txtPrateleira;
    private JTextField txtGrupo;
    private JTextField txtCodigoVenda;
    private JTextArea txtCodigosOriginais;
    private JTextField txtLargura;
    private JTextField txtAltura;

    private JButton btnModificar;
    private JButton btnExcluir;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnCancelar;
    private JButton btnFechar;

    public TelaDetalheEtiqueta(JFrame telaPai, Etiqueta etiqueta, Runnable aoAtualizar) {
        super(telaPai, "Detalhes da Etiqueta", true);

        this.etiqueta = etiqueta;
        this.aoAtualizar = aoAtualizar;
        this.etiquetaBO = new EtiquetaBO();

        setSize(680, 570);
        setLocationRelativeTo(telaPai);
        setResizable(false);

        criarComponentes();
        preencherCampos();
        bloquearCampos();
        configurarModoVisualizacao();
    }

    private void criarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Detalhes da Etiqueta", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel painelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField();
        txtDescricao = new JTextField();
        txtPrateleira = new JTextField();
        txtGrupo = new JTextField();
        txtCodigoVenda = new JTextField();
        txtCodigosOriginais = new JTextArea(4, 20);
        txtLargura = new JTextField();
        txtAltura = new JTextField();

        JScrollPane scrollCodigos = new JScrollPane(txtCodigosOriginais);

        adicionarCampoTexto(painelCampos, gbc, 0, "ID:", txtId);
        adicionarCampoTexto(painelCampos, gbc, 1, "Descrição:", txtDescricao);
        adicionarCampoTexto(painelCampos, gbc, 2, "Prateleira:", txtPrateleira);
        adicionarCampoTexto(painelCampos, gbc, 3, "Grupo:", txtGrupo);
        adicionarCampoTexto(painelCampos, gbc, 4, "Código venda:", txtCodigoVenda);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        painelCampos.add(new JLabel("Códigos originais:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1;
        painelCampos.add(scrollCodigos, gbc);

        adicionarCampoTexto(painelCampos, gbc, 6, "Largura cm:", txtLargura);
        adicionarCampoTexto(painelCampos, gbc, 7, "Altura cm:", txtAltura);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnModificar = new JButton("MODIFICAR");
        btnExcluir = new JButton("EXCLUIR");
        btnNovo = new JButton("NOVO");
        btnSalvar = new JButton("SALVAR");
        btnCancelar = new JButton("CANCELAR");
        btnFechar = new JButton("FECHAR");

        btnModificar.addActionListener(e -> ativarEdicao());
        btnExcluir.addActionListener(e -> excluirEtiqueta());
        btnNovo.addActionListener(e -> novaEtiqueta());
        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnCancelar.addActionListener(e -> cancelarEdicao());
        btnFechar.addActionListener(e -> dispose());

        painelBotoes.add(btnModificar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnFechar);

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

    private void preencherCampos() {
        txtId.setText(String.valueOf(etiqueta.getId()));
        txtDescricao.setText(etiqueta.getDescricao());
        txtPrateleira.setText(etiqueta.getPrateleira().getLocalPrateleira());
        txtGrupo.setText(etiqueta.getPrateleira().getDescricaoGrupo());
        txtCodigoVenda.setText(etiqueta.getCodigoVenda());

        if (etiqueta.getCodigosOriginais() != null) {
            txtCodigosOriginais.setText(String.join("\n", etiqueta.getCodigosOriginais()));
        } else {
            txtCodigosOriginais.setText("");
        }

        txtLargura.setText(String.valueOf(etiqueta.getLarguraCm()).replace(".", ","));
        txtAltura.setText(String.valueOf(etiqueta.getAlturaCm()).replace(".", ","));
    }

    private void bloquearCampos() {
        txtId.setEditable(false);
        txtDescricao.setEditable(false);
        txtPrateleira.setEditable(false);
        txtGrupo.setEditable(false);
        txtCodigoVenda.setEditable(false);
        txtCodigosOriginais.setEditable(false);
        txtLargura.setEditable(false);
        txtAltura.setEditable(false);
        definirCorCamposBloqueados();
    }

    private void liberarCampos() {
        txtId.setEditable(false);
        txtDescricao.setEditable(true);
        txtPrateleira.setEditable(true);
        txtGrupo.setEditable(true);
        txtCodigoVenda.setEditable(true);
        txtCodigosOriginais.setEditable(true);
        txtLargura.setEditable(true);
        txtAltura.setEditable(true);
        definirCorCamposEditaveis();
    }

    private void definirCorCamposBloqueados() {
        Color cor = new Color(240, 240, 240);
        txtId.setBackground(cor);
        txtDescricao.setBackground(cor);
        txtPrateleira.setBackground(cor);
        txtGrupo.setBackground(cor);
        txtCodigoVenda.setBackground(cor);
        txtCodigosOriginais.setBackground(cor);
        txtLargura.setBackground(cor);
        txtAltura.setBackground(cor);
    }

    private void definirCorCamposEditaveis() {
        Color cor = Color.WHITE;
        txtDescricao.setBackground(cor);
        txtPrateleira.setBackground(cor);
        txtGrupo.setBackground(cor);
        txtCodigoVenda.setBackground(cor);
        txtCodigosOriginais.setBackground(cor);
        txtLargura.setBackground(cor);
        txtAltura.setBackground(cor);
        txtId.setBackground(new Color(240, 240, 240));
    }

    private void configurarModoVisualizacao() {
        btnModificar.setVisible(true);
        btnExcluir.setVisible(true);
        btnNovo.setVisible(true);
        btnFechar.setVisible(true);
        btnSalvar.setVisible(false);
        btnCancelar.setVisible(false);
    }

    private void configurarModoEdicao() {
        btnModificar.setVisible(false);
        btnExcluir.setVisible(false);
        btnNovo.setVisible(false);
        btnFechar.setVisible(false);
        btnSalvar.setVisible(true);
        btnCancelar.setVisible(true);
    }

    private void ativarEdicao() {
        liberarCampos();
        configurarModoEdicao();
    }

    private void cancelarEdicao() {
        preencherCampos();
        bloquearCampos();
        configurarModoVisualizacao();
    }

    private void salvarAlteracoes() {
        try {
            String descricao = txtDescricao.getText().trim();
            String prateleira = txtPrateleira.getText().trim();
            String grupo = txtGrupo.getText().trim();
            String codigoVenda = txtCodigoVenda.getText().trim();
            String codigosTexto = txtCodigosOriginais.getText().trim();
            double largura = converterNumero(txtLargura.getText());
            double altura = converterNumero(txtAltura.getText());

            if (descricao.isBlank()) {
                mostrarAviso("A descrição é obrigatória.");
                return;
            }

            if (prateleira.isBlank()) {
                mostrarAviso("A prateleira é obrigatória.");
                return;
            }

            if (codigoVenda.isBlank()) {
                mostrarAviso("O código de venda é obrigatório.");
                return;
            }

            if (largura <= 0 || altura <= 0) {
                mostrarAviso("Largura e altura precisam ser maiores que zero.");
                return;
            }

            etiqueta.setDescricao(descricao);
            etiqueta.getPrateleira().setLocalPrateleira(prateleira);
            etiqueta.getPrateleira().setDescricaoGrupo(grupo);
            etiqueta.setCodigoVenda(codigoVenda);
            etiqueta.setCodigosOriginais(converterCodigosOriginais(codigosTexto));
            etiqueta.setLarguraCm(largura);
            etiqueta.setAlturaCm(altura);

            etiquetaBO.atualizarEtiqueta(etiqueta);

            JOptionPane.showMessageDialog(this, "Etiqueta atualizada com sucesso!");

            bloquearCampos();
            configurarModoVisualizacao();

            if (aoAtualizar != null) {
                aoAtualizar.run();
            }
        } catch (NumberFormatException e) {
            mostrarAviso("Largura e altura precisam ser números. Exemplo: 12 ou 6,5.");
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao salvar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirEtiqueta() {
        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir esta etiqueta?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            etiquetaBO.excluirEtiqueta(etiqueta.getId());
            JOptionPane.showMessageDialog(this, "Etiqueta excluída com sucesso!");

            if (aoAtualizar != null) {
                aoAtualizar.run();
            }

            dispose();
        }
    }

    private void novaEtiqueta() {
        TelaFormularioEtiqueta tela = new TelaFormularioEtiqueta((JFrame) getOwner(), aoAtualizar);
        tela.setVisible(true);
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
