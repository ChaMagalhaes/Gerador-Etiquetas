package dev.charles.gerador_etiquetas.view;

import dev.charles.gerador_etiquetas.controller.FabricanteController;
import dev.charles.gerador_etiquetas.model.Fabricante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaCadastroFabricante extends JDialog {

    private final FabricanteController fabricanteController;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtTipo;
    private JCheckBox chkAtivo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private List<Fabricante> fabricantes;

    public TelaCadastroFabricante(JFrame telaPai) {
        super(telaPai, "Cadastro de Fabricantes", true);
        this.fabricanteController = new FabricanteController();

        setSize(780, 520);
        setLocationRelativeTo(telaPai);
        criarComponentes();
        carregarFabricantes();
    }

    private void criarComponentes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel titulo = new JLabel("Fabricantes", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField();
        txtId.setEditable(false);
        txtNome = new JTextField();
        txtTipo = new JTextField();
        chkAtivo = new JCheckBox("Ativo", true);

        adicionarCampo(form, gbc, 0, "ID:", txtId);
        adicionarCampo(form, gbc, 1, "Nome:", txtNome);
        adicionarCampo(form, gbc, 2, "Tipo:", txtTipo);

        gbc.gridx = 1;
        gbc.gridy = 3;
        form.add(chkAtivo, gbc);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNovo = new JButton("Novo/Limpar");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnRecarregar = new JButton("Recarregar");

        btnNovo.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(e -> salvar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnRecarregar.addActionListener(e -> carregarFabricantes());

        botoes.add(btnNovo);
        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);
        botoes.add(btnRecarregar);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(titulo, BorderLayout.NORTH);
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Tipo", "Ativo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> preencherSelecionado());

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

    private void carregarFabricantes() {
        try {
            fabricantes = fabricanteController.listarFabricantes();
            modeloTabela.setRowCount(0);
            for (Fabricante fabricante : fabricantes) {
                modeloTabela.addRow(new Object[]{
                        fabricante.getId(),
                        fabricante.getNome(),
                        fabricante.getTipo(),
                        fabricante.getAtivo() != null && fabricante.getAtivo() ? "Sim" : "Não"
                });
            }
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void salvar() {
        try {
            fabricanteController.cadastrarFabricante(txtNome.getText(), txtTipo.getText());
            JOptionPane.showMessageDialog(this, "Fabricante cadastrado com sucesso!");
            limparCampos();
            carregarFabricantes();
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void atualizar() {
        try {
            Long id = obterIdCampo();
            if (id == null) {
                mostrarAviso("Selecione um fabricante para atualizar.");
                return;
            }

            Fabricante fabricante = new Fabricante();
            fabricante.setId(id);
            fabricante.setNome(txtNome.getText());
            fabricante.setTipo(txtTipo.getText());
            fabricante.setAtivo(chkAtivo.isSelected());

            fabricanteController.atualizarFabricante(fabricante);
            JOptionPane.showMessageDialog(this, "Fabricante atualizado com sucesso!");
            limparCampos();
            carregarFabricantes();
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void excluir() {
        try {
            Long id = obterIdCampo();
            if (id == null) {
                mostrarAviso("Selecione um fabricante para excluir.");
                return;
            }

            int opcao = JOptionPane.showConfirmDialog(this, "Deseja excluir o fabricante selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                fabricanteController.excluirFabricante(id);
                JOptionPane.showMessageDialog(this, "Fabricante excluído com sucesso!");
                limparCampos();
                carregarFabricantes();
            }
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void preencherSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || fabricantes == null || linha >= fabricantes.size()) {
            return;
        }

        Fabricante fabricante = fabricantes.get(tabela.convertRowIndexToModel(linha));
        txtId.setText(String.valueOf(fabricante.getId()));
        txtNome.setText(fabricante.getNome());
        txtTipo.setText(fabricante.getTipo());
        chkAtivo.setSelected(fabricante.getAtivo() != null && fabricante.getAtivo());
    }

    private Long obterIdCampo() {
        if (txtId.getText() == null || txtId.getText().isBlank()) {
            return null;
        }
        return Long.parseLong(txtId.getText());
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtTipo.setText("");
        chkAtivo.setSelected(true);
        tabela.clearSelection();
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }
}
