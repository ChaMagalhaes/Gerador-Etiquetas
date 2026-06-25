package dev.charles.gerador_etiquetas.view;

import dev.charles.gerador_etiquetas.controller.EtiquetaController;
import dev.charles.gerador_etiquetas.controller.FabricanteController;
import dev.charles.gerador_etiquetas.controller.GrupoController;
import dev.charles.gerador_etiquetas.controller.PrateleiraController;
import dev.charles.gerador_etiquetas.controller.SubGrupoController;
import dev.charles.gerador_etiquetas.model.Fabricante;
import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.model.Prateleira;
import dev.charles.gerador_etiquetas.model.SubGrupo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelaFormularioEtiqueta extends JDialog {

    private final EtiquetaController etiquetaController;
    private final PrateleiraController prateleiraController;
    private final GrupoController grupoController;
    private final SubGrupoController subGrupoController;
    private final FabricanteController fabricanteController;
    private final Runnable aoSalvar;

    private JTextField txtDescricao;
    private JComboBox<Prateleira> comboPrateleira;
    private JComboBox<Grupo> comboGrupo;
    private JComboBox<SubGrupo> comboSubGrupo;
    private JComboBox<Fabricante> comboFabricante;
    private JTextField txtCodigoVenda;
    private JTextArea txtCodigosOriginais;
    private JTextField txtLargura;
    private JTextField txtAltura;

    public TelaFormularioEtiqueta(JFrame telaPai, Runnable aoSalvar) {
        super(telaPai, "Nova Etiqueta", true);

        this.etiquetaController = new EtiquetaController();
        this.prateleiraController = new PrateleiraController();
        this.grupoController = new GrupoController();
        this.subGrupoController = new SubGrupoController();
        this.fabricanteController = new FabricanteController();
        this.aoSalvar = aoSalvar;

        setSize(760, 650);
        setLocationRelativeTo(telaPai);
        setResizable(false);

        criarComponentes();
        carregarCombos();
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
        comboPrateleira = new JComboBox<>();
        comboGrupo = new JComboBox<>();
        comboSubGrupo = new JComboBox<>();
        comboFabricante = new JComboBox<>();
        txtCodigoVenda = new JTextField();
        txtCodigosOriginais = new JTextArea(5, 20);
        txtLargura = new JTextField("12");
        txtAltura = new JTextField("6");

        configurarRenderer(comboPrateleira, "Selecione uma prateleira");
        configurarRenderer(comboGrupo, "Sem grupo");
        configurarRenderer(comboSubGrupo, "Sem subgrupo");
        configurarRenderer(comboFabricante, "Sem fabricante");

        comboGrupo.addActionListener(e -> carregarSubgruposDoGrupo());

        JScrollPane scrollCodigos = new JScrollPane(txtCodigosOriginais);

        adicionarCampoTexto(painelCampos, gbc, 0, "Descrição:", txtDescricao);
        adicionarCampoCombo(painelCampos, gbc, 1, "Prateleira:", comboPrateleira);
        adicionarCampoCombo(painelCampos, gbc, 2, "Grupo:", comboGrupo);
        adicionarCampoCombo(painelCampos, gbc, 3, "Subgrupo:", comboSubGrupo);
        adicionarCampoCombo(painelCampos, gbc, 4, "Fabricante:", comboFabricante);
        adicionarCampoTexto(painelCampos, gbc, 5, "Código venda:", txtCodigoVenda);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        painelCampos.add(new JLabel("Códigos originais:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painelCampos.add(scrollCodigos, gbc);
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        adicionarCampoTexto(painelCampos, gbc, 7, "Largura cm:", txtLargura);
        adicionarCampoTexto(painelCampos, gbc, 8, "Altura cm:", txtAltura);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSalvar = new JButton("SALVAR");
        JButton btnCancelar = new JButton("CANCELAR");

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        JLabel dica = new JLabel("Dica: separe os códigos originais por linha, vírgula ou ponto e vírgula.");
        dica.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.add(dica, BorderLayout.NORTH);
        rodape.add(painelBotoes, BorderLayout.SOUTH);

        painelPrincipal.add(titulo, BorderLayout.NORTH);
        painelPrincipal.add(painelCampos, BorderLayout.CENTER);
        painelPrincipal.add(rodape, BorderLayout.SOUTH);

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
        Object selecionado = comboGrupo.getSelectedItem();
        comboSubGrupo.removeAllItems();
        comboSubGrupo.addItem(null);

        if (selecionado instanceof Grupo grupo && grupo.getId() != null) {
            for (SubGrupo subGrupo : subGrupoController.listarPorGrupo(grupo.getId())) {
                comboSubGrupo.addItem(subGrupo);
            }
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

    private void salvar() {
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

            etiquetaController.cadastrarEtiqueta(
                    descricao,
                    prateleira,
                    grupo,
                    subGrupo,
                    fabricante,
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
