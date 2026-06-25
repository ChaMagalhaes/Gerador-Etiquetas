package dev.charles.gerador_etiquetas.view;

import dev.charles.gerador_etiquetas.bo.EtiquetaBO;
import dev.charles.gerador_etiquetas.controller.FabricanteController;
import dev.charles.gerador_etiquetas.controller.GrupoController;
import dev.charles.gerador_etiquetas.controller.PrateleiraController;
import dev.charles.gerador_etiquetas.controller.SubGrupoController;
import dev.charles.gerador_etiquetas.model.Etiqueta;
import dev.charles.gerador_etiquetas.model.EtiquetaCodigoOriginal;
import dev.charles.gerador_etiquetas.model.Fabricante;
import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.model.Prateleira;
import dev.charles.gerador_etiquetas.model.SubGrupo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelaDetalheEtiqueta extends JDialog {

    private final Etiqueta etiqueta;
    private final EtiquetaBO etiquetaBO;
    private final PrateleiraController prateleiraController;
    private final GrupoController grupoController;
    private final SubGrupoController subGrupoController;
    private final FabricanteController fabricanteController;
    private final Runnable aoAtualizar;

    private JTextField txtId;
    private JTextField txtDescricao;
    private JComboBox<Prateleira> comboPrateleira;
    private JComboBox<Grupo> comboGrupo;
    private JComboBox<SubGrupo> comboSubGrupo;
    private JComboBox<Fabricante> comboFabricante;
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
        this.prateleiraController = new PrateleiraController();
        this.grupoController = new GrupoController();
        this.subGrupoController = new SubGrupoController();
        this.fabricanteController = new FabricanteController();

        setSize(790, 680);
        setLocationRelativeTo(telaPai);
        setResizable(false);

        criarComponentes();
        carregarCombos();
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
        comboPrateleira = new JComboBox<>();
        comboGrupo = new JComboBox<>();
        comboSubGrupo = new JComboBox<>();
        comboFabricante = new JComboBox<>();
        txtCodigoVenda = new JTextField();
        txtCodigosOriginais = new JTextArea(5, 20);
        txtLargura = new JTextField();
        txtAltura = new JTextField();

        configurarRenderer(comboPrateleira, "Selecione uma prateleira");
        configurarRenderer(comboGrupo, "Sem grupo");
        configurarRenderer(comboSubGrupo, "Sem subgrupo");
        configurarRenderer(comboFabricante, "Sem fabricante");

        comboGrupo.addActionListener(e -> carregarSubgruposDoGrupo());

        JScrollPane scrollCodigos = new JScrollPane(txtCodigosOriginais);

        adicionarCampoTexto(painelCampos, gbc, 0, "ID:", txtId);
        adicionarCampoTexto(painelCampos, gbc, 1, "Descrição:", txtDescricao);
        adicionarCampoCombo(painelCampos, gbc, 2, "Prateleira:", comboPrateleira);
        adicionarCampoCombo(painelCampos, gbc, 3, "Grupo:", comboGrupo);
        adicionarCampoCombo(painelCampos, gbc, 4, "Subgrupo:", comboSubGrupo);
        adicionarCampoCombo(painelCampos, gbc, 5, "Fabricante:", comboFabricante);
        adicionarCampoTexto(painelCampos, gbc, 6, "Código venda:", txtCodigoVenda);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0;
        painelCampos.add(new JLabel("Códigos originais:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painelCampos.add(scrollCodigos, gbc);
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        adicionarCampoTexto(painelCampos, gbc, 8, "Largura cm:", txtLargura);
        adicionarCampoTexto(painelCampos, gbc, 9, "Altura cm:", txtAltura);

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

    private void carregarCombos() {
        carregarPrateleiras();
        carregarGrupos();
        carregarFabricantes();
        carregarSubgruposDoGrupo();
    }

    private void carregarPrateleiras() {
        comboPrateleira.removeAllItems();
        comboPrateleira.addItem(null);
        for (Prateleira prateleira : prateleiraController.listarPrateleiras()) {
            comboPrateleira.addItem(prateleira);
        }
    }

    private void carregarGrupos() {
        comboGrupo.removeAllItems();
        comboGrupo.addItem(null);
        for (Grupo grupo : grupoController.listarGrupos()) {
            comboGrupo.addItem(grupo);
        }
    }

    private void carregarSubgruposDoGrupo() {
        Long subGrupoAtualId = comboSubGrupo.getSelectedItem() instanceof SubGrupo sg ? sg.getId() : null;
        comboSubGrupo.removeAllItems();
        comboSubGrupo.addItem(null);

        Object selecionado = comboGrupo.getSelectedItem();
        if (selecionado instanceof Grupo grupo && grupo.getId() != null) {
            for (SubGrupo subGrupo : subGrupoController.listarPorGrupo(grupo.getId())) {
                comboSubGrupo.addItem(subGrupo);
            }
        }

        if (subGrupoAtualId != null) {
            selecionarPorId(comboSubGrupo, subGrupoAtualId);
        }
    }

    private void carregarFabricantes() {
        comboFabricante.removeAllItems();
        comboFabricante.addItem(null);
        for (Fabricante fabricante : fabricanteController.listarFabricantesAtivos()) {
            comboFabricante.addItem(fabricante);
        }
    }

    private void adicionarCampoTexto(JPanel painel, GridBagConstraints gbc, int linha, String label, JTextField campo) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(140, 25));
        painel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.gridy = linha;
        gbc.weightx = 1;
        painel.add(campo, gbc);
    }

    private void adicionarCampoCombo(JPanel painel, GridBagConstraints gbc, int linha, String label, JComboBox<?> combo) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(140, 25));
        painel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.gridy = linha;
        gbc.weightx = 1;
        painel.add(combo, gbc);
    }

    private void preencherCampos() {
        txtId.setText(String.valueOf(etiqueta.getId()));
        txtDescricao.setText(etiqueta.getDescricao());
        txtCodigoVenda.setText(etiqueta.getCodigoVenda());
        txtCodigosOriginais.setText(formatarCodigosSomenteTexto(etiqueta.getCodigosOriginais()));
        txtLargura.setText(String.valueOf(etiqueta.getLarguraCm()).replace(".", ","));
        txtAltura.setText(String.valueOf(etiqueta.getAlturaCm()).replace(".", ","));

        selecionarPorId(comboPrateleira, etiqueta.getPrateleira() != null ? etiqueta.getPrateleira().getId() : null);
        selecionarPorId(comboGrupo, etiqueta.getGrupo() != null ? etiqueta.getGrupo().getId() : null);
        carregarSubgruposDoGrupo();
        selecionarPorId(comboSubGrupo, etiqueta.getSubGrupo() != null ? etiqueta.getSubGrupo().getId() : null);
        selecionarPorId(comboFabricante, obterPrimeiroFabricanteId(etiqueta.getCodigosOriginais()));
    }

    private void bloquearCampos() {
        txtId.setEditable(false);
        txtDescricao.setEditable(false);
        comboPrateleira.setEnabled(false);
        comboGrupo.setEnabled(false);
        comboSubGrupo.setEnabled(false);
        comboFabricante.setEnabled(false);
        txtCodigoVenda.setEditable(false);
        txtCodigosOriginais.setEditable(false);
        txtLargura.setEditable(false);
        txtAltura.setEditable(false);
        definirCorCamposBloqueados();
    }

    private void liberarCampos() {
        txtId.setEditable(false);
        txtDescricao.setEditable(true);
        comboPrateleira.setEnabled(true);
        comboGrupo.setEnabled(true);
        comboSubGrupo.setEnabled(true);
        comboFabricante.setEnabled(true);
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
        txtCodigoVenda.setBackground(cor);
        txtCodigosOriginais.setBackground(cor);
        txtLargura.setBackground(cor);
        txtAltura.setBackground(cor);
    }

    private void definirCorCamposEditaveis() {
        Color cor = Color.WHITE;
        txtDescricao.setBackground(cor);
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
            Prateleira prateleira = (Prateleira) comboPrateleira.getSelectedItem();
            Grupo grupo = (Grupo) comboGrupo.getSelectedItem();
            SubGrupo subGrupo = (SubGrupo) comboSubGrupo.getSelectedItem();
            Fabricante fabricante = (Fabricante) comboFabricante.getSelectedItem();
            String codigoVenda = txtCodigoVenda.getText().trim();
            String codigosTexto = txtCodigosOriginais.getText().trim();
            double largura = converterNumero(txtLargura.getText());
            double altura = converterNumero(txtAltura.getText());

            etiqueta.setDescricao(descricao);
            etiqueta.setPrateleira(prateleira);
            etiqueta.setGrupo(grupo);
            etiqueta.setSubGrupo(subGrupo);
            etiqueta.setCodigoVenda(codigoVenda);
            etiqueta.setCodigosOriginais(converterCodigosOriginais(codigosTexto, fabricante));
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

    private List<EtiquetaCodigoOriginal> converterCodigosOriginais(String texto, Fabricante fabricante) {
        List<EtiquetaCodigoOriginal> codigos = new ArrayList<>();

        if (texto == null || texto.isBlank()) {
            return codigos;
        }

        String textoFormatado = texto.replace(";", ",").replace("\n", ",");

        Arrays.stream(textoFormatado.split(","))
                .map(String::trim)
                .filter(codigo -> !codigo.isBlank())
                .forEach(codigoTexto -> {
                    EtiquetaCodigoOriginal codigo = new EtiquetaCodigoOriginal();
                    codigo.setCodigoOriginal(codigoTexto);
                    codigo.setFabricante(fabricante);
                    codigos.add(codigo);
                });

        return codigos;
    }

    private String formatarCodigosSomenteTexto(List<EtiquetaCodigoOriginal> codigos) {
        if (codigos == null || codigos.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (EtiquetaCodigoOriginal codigo : codigos) {
            if (codigo != null && codigo.getCodigoOriginal() != null) {
                if (!builder.isEmpty()) {
                    builder.append("\n");
                }
                builder.append(codigo.getCodigoOriginal());
            }
        }

        return builder.toString();
    }

    private Long obterPrimeiroFabricanteId(List<EtiquetaCodigoOriginal> codigos) {
        if (codigos == null) {
            return null;
        }

        for (EtiquetaCodigoOriginal codigo : codigos) {
            if (codigo != null && codigo.getFabricante() != null) {
                return codigo.getFabricante().getId();
            }
        }

        return null;
    }

    private double converterNumero(String texto) {
        return Double.parseDouble(texto.trim().replace(",", "."));
    }

    private void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    private <T> void selecionarPorId(JComboBox<T> combo, Long id) {
        if (id == null) {
            combo.setSelectedItem(null);
            return;
        }

        for (int i = 0; i < combo.getItemCount(); i++) {
            T item = combo.getItemAt(i);
            Long itemId = obterId(item);
            if (id.equals(itemId)) {
                combo.setSelectedIndex(i);
                return;
            }
        }

        combo.setSelectedItem(null);
    }

    private Long obterId(Object item) {
        if (item instanceof Prateleira prateleira) {
            return prateleira.getId();
        }
        if (item instanceof Grupo grupo) {
            return grupo.getId();
        }
        if (item instanceof SubGrupo subGrupo) {
            return subGrupo.getId();
        }
        if (item instanceof Fabricante fabricante) {
            return fabricante.getId();
        }
        return null;
    }

    private void configurarRenderer(JComboBox<?> combo, String textoNulo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value == null ? textoNulo : value.toString());
                return this;
            }
        });
    }
}
