package dev.charles.gerador_etiquetas.view;

import dev.charles.gerador_etiquetas.controller.GrupoController;
import dev.charles.gerador_etiquetas.controller.SubGrupoController;
import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.model.SubGrupo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaCadastroGrupoSubGrupo extends JDialog {

    private final GrupoController grupoController;
    private final SubGrupoController subGrupoController;

    private JTextField txtGrupoId;
    private JTextField txtGrupoDescricao;
    private JTable tabelaGrupos;
    private DefaultTableModel modeloGrupos;
    private List<Grupo> grupos;

    private JTextField txtSubGrupoId;
    private JTextField txtSubGrupoDescricao;
    private JComboBox<Grupo> comboGrupoSubgrupo;
    private JTable tabelaSubGrupos;
    private DefaultTableModel modeloSubGrupos;
    private List<SubGrupo> subGrupos;

    public TelaCadastroGrupoSubGrupo(JFrame telaPai) {
        super(telaPai, "Cadastro de Grupos e Subgrupos", true);
        this.grupoController = new GrupoController();
        this.subGrupoController = new SubGrupoController();

        setSize(900, 620);
        setLocationRelativeTo(telaPai);
        criarComponentes();
        carregarGrupos();
        carregarSubGrupos();
    }

    private void criarComponentes() {
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Grupos", criarPainelGrupos());
        abas.addTab("Subgrupos", criarPainelSubGrupos());
        add(abas);
    }

    private JPanel criarPainelGrupos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtGrupoId = new JTextField();
        txtGrupoId.setEditable(false);
        txtGrupoDescricao = new JTextField();

        adicionarCampo(form, gbc, 0, "ID:", txtGrupoId);
        adicionarCampo(form, gbc, 1, "Descrição:", txtGrupoDescricao);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNovo = new JButton("Novo/Limpar");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnRecarregar = new JButton("Recarregar");

        btnNovo.addActionListener(e -> limparGrupo());
        btnSalvar.addActionListener(e -> salvarGrupo());
        btnAtualizar.addActionListener(e -> atualizarGrupo());
        btnExcluir.addActionListener(e -> excluirGrupo());
        btnRecarregar.addActionListener(e -> carregarGrupos());

        botoes.add(btnNovo);
        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);
        botoes.add(btnRecarregar);

        modeloGrupos = new DefaultTableModel(new String[]{"ID", "Descrição"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaGrupos = new JTable(modeloGrupos);
        tabelaGrupos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaGrupos.getSelectionModel().addListSelectionListener(e -> preencherGrupoSelecionado());

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaGrupos), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelSubGrupos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtSubGrupoId = new JTextField();
        txtSubGrupoId.setEditable(false);
        txtSubGrupoDescricao = new JTextField();
        comboGrupoSubgrupo = new JComboBox<>();
        configurarRenderer(comboGrupoSubgrupo, "Selecione o grupo");

        adicionarCampo(form, gbc, 0, "ID:", txtSubGrupoId);
        adicionarCampoCombo(form, gbc, 1, "Grupo:", comboGrupoSubgrupo);
        adicionarCampo(form, gbc, 2, "Descrição:", txtSubGrupoDescricao);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNovo = new JButton("Novo/Limpar");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnRecarregar = new JButton("Recarregar");

        btnNovo.addActionListener(e -> limparSubGrupo());
        btnSalvar.addActionListener(e -> salvarSubGrupo());
        btnAtualizar.addActionListener(e -> atualizarSubGrupo());
        btnExcluir.addActionListener(e -> excluirSubGrupo());
        btnRecarregar.addActionListener(e -> carregarSubGrupos());

        botoes.add(btnNovo);
        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);
        botoes.add(btnRecarregar);

        modeloSubGrupos = new DefaultTableModel(new String[]{"ID", "Grupo", "Subgrupo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaSubGrupos = new JTable(modeloSubGrupos);
        tabelaSubGrupos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaSubGrupos.getSelectionModel().addListSelectionListener(e -> preencherSubGrupoSelecionado());

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaSubGrupos), BorderLayout.CENTER);
        return painel;
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

    private void adicionarCampoCombo(JPanel painel, GridBagConstraints gbc, int linha, String label, JComboBox<?> combo) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;
        painel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.gridy = linha;
        gbc.weightx = 1;
        painel.add(combo, gbc);
    }

    private void carregarGrupos() {
        try {
            grupos = grupoController.listarGrupos();
            modeloGrupos.setRowCount(0);
            comboGrupoSubgrupo.removeAllItems();
            comboGrupoSubgrupo.addItem(null);

            for (Grupo grupo : grupos) {
                modeloGrupos.addRow(new Object[]{grupo.getId(), grupo.getDescricao()});
                comboGrupoSubgrupo.addItem(grupo);
            }
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void carregarSubGrupos() {
        try {
            subGrupos = subGrupoController.listarSubgrupos();
            modeloSubGrupos.setRowCount(0);
            for (SubGrupo subGrupo : subGrupos) {
                modeloSubGrupos.addRow(new Object[]{
                        subGrupo.getId(),
                        subGrupo.getGrupo() != null ? subGrupo.getGrupo().getDescricao() : "",
                        subGrupo.getDescricao()
                });
            }
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void salvarGrupo() {
        try {
            grupoController.cadastrarGrupo(txtGrupoDescricao.getText());
            JOptionPane.showMessageDialog(this, "Grupo cadastrado com sucesso!");
            limparGrupo();
            carregarGrupos();
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void atualizarGrupo() {
        try {
            Long id = obterLong(txtGrupoId.getText());
            if (id == null) {
                mostrarAviso("Selecione um grupo para atualizar.");
                return;
            }

            Grupo grupo = new Grupo();
            grupo.setId(id);
            grupo.setDescricao(txtGrupoDescricao.getText());
            grupoController.atualizarGrupo(grupo);

            JOptionPane.showMessageDialog(this, "Grupo atualizado com sucesso!");
            limparGrupo();
            carregarGrupos();
            carregarSubGrupos();
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void excluirGrupo() {
        try {
            Long id = obterLong(txtGrupoId.getText());
            if (id == null) {
                mostrarAviso("Selecione um grupo para excluir.");
                return;
            }

            int opcao = JOptionPane.showConfirmDialog(this, "Deseja excluir o grupo selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                grupoController.excluirGrupo(id);
                JOptionPane.showMessageDialog(this, "Grupo excluído com sucesso!");
                limparGrupo();
                carregarGrupos();
                carregarSubGrupos();
            }
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void salvarSubGrupo() {
        try {
            Grupo grupo = (Grupo) comboGrupoSubgrupo.getSelectedItem();
            subGrupoController.cadastrarSubgrupo(grupo, txtSubGrupoDescricao.getText());
            JOptionPane.showMessageDialog(this, "Subgrupo cadastrado com sucesso!");
            limparSubGrupo();
            carregarSubGrupos();
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void atualizarSubGrupo() {
        try {
            Long id = obterLong(txtSubGrupoId.getText());
            if (id == null) {
                mostrarAviso("Selecione um subgrupo para atualizar.");
                return;
            }

            SubGrupo subGrupo = new SubGrupo();
            subGrupo.setId(id);
            subGrupo.setGrupo((Grupo) comboGrupoSubgrupo.getSelectedItem());
            subGrupo.setDescricao(txtSubGrupoDescricao.getText());

            subGrupoController.atualizarSubgrupo(subGrupo);
            JOptionPane.showMessageDialog(this, "Subgrupo atualizado com sucesso!");
            limparSubGrupo();
            carregarSubGrupos();
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void excluirSubGrupo() {
        try {
            Long id = obterLong(txtSubGrupoId.getText());
            if (id == null) {
                mostrarAviso("Selecione um subgrupo para excluir.");
                return;
            }

            int opcao = JOptionPane.showConfirmDialog(this, "Deseja excluir o subgrupo selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                subGrupoController.excluirSubgrupo(id);
                JOptionPane.showMessageDialog(this, "Subgrupo excluído com sucesso!");
                limparSubGrupo();
                carregarSubGrupos();
            }
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void preencherGrupoSelecionado() {
        int linha = tabelaGrupos.getSelectedRow();
        if (linha < 0 || grupos == null) {
            return;
        }
        Grupo grupo = grupos.get(tabelaGrupos.convertRowIndexToModel(linha));
        txtGrupoId.setText(String.valueOf(grupo.getId()));
        txtGrupoDescricao.setText(grupo.getDescricao());
    }

    private void preencherSubGrupoSelecionado() {
        int linha = tabelaSubGrupos.getSelectedRow();
        if (linha < 0 || subGrupos == null) {
            return;
        }
        SubGrupo subGrupo = subGrupos.get(tabelaSubGrupos.convertRowIndexToModel(linha));
        txtSubGrupoId.setText(String.valueOf(subGrupo.getId()));
        txtSubGrupoDescricao.setText(subGrupo.getDescricao());
        selecionarGrupoCombo(subGrupo.getGrupo() != null ? subGrupo.getGrupo().getId() : null);
    }

    private void selecionarGrupoCombo(Long grupoId) {
        if (grupoId == null) {
            comboGrupoSubgrupo.setSelectedItem(null);
            return;
        }
        for (int i = 0; i < comboGrupoSubgrupo.getItemCount(); i++) {
            Grupo grupo = comboGrupoSubgrupo.getItemAt(i);
            if (grupo != null && grupoId.equals(grupo.getId())) {
                comboGrupoSubgrupo.setSelectedIndex(i);
                return;
            }
        }
        comboGrupoSubgrupo.setSelectedItem(null);
    }

    private void limparGrupo() {
        txtGrupoId.setText("");
        txtGrupoDescricao.setText("");
        tabelaGrupos.clearSelection();
    }

    private void limparSubGrupo() {
        txtSubGrupoId.setText("");
        txtSubGrupoDescricao.setText("");
        comboGrupoSubgrupo.setSelectedItem(null);
        tabelaSubGrupos.clearSelection();
    }

    private Long obterLong(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        return Long.parseLong(texto);
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
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
