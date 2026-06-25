package dev.charles.gerador_etiquetas.view;

import dev.charles.gerador_etiquetas.controller.PrateleiraController;
import dev.charles.gerador_etiquetas.model.Prateleira;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaCadastroPrateleira extends JDialog {

    private final PrateleiraController prateleiraController;

    private JTextField txtId;
    private JTextField txtLocal;
    private JTextField txtDescricaoGrupo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private List<Prateleira> prateleiras;

    public TelaCadastroPrateleira(JFrame telaPai) {
        super(telaPai, "Cadastro de Prateleiras", true);
        this.prateleiraController = new PrateleiraController();

        setSize(780, 500);
        setLocationRelativeTo(telaPai);
        criarComponentes();
        carregarPrateleiras();
    }

    private void criarComponentes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel titulo = new JLabel("Prateleiras", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField();
        txtId.setEditable(false);
        txtLocal = new JTextField();
        txtDescricaoGrupo = new JTextField();

        adicionarCampo(form, gbc, 0, "ID:", txtId);
        adicionarCampo(form, gbc, 1, "Local:", txtLocal);
        adicionarCampo(form, gbc, 2, "Descrição/grupo:", txtDescricaoGrupo);

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
        btnRecarregar.addActionListener(e -> carregarPrateleiras());

        botoes.add(btnNovo);
        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);
        botoes.add(btnRecarregar);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(titulo, BorderLayout.NORTH);
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        modeloTabela = new DefaultTableModel(new String[]{"ID", "Local", "Descrição/grupo"}, 0) {
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

    private void carregarPrateleiras() {
        try {
            prateleiras = prateleiraController.listarPrateleiras();
            modeloTabela.setRowCount(0);
            for (Prateleira prateleira : prateleiras) {
                modeloTabela.addRow(new Object[]{
                        prateleira.getId(),
                        prateleira.getLocalPrateleira(),
                        prateleira.getDescricaoGrupo()
                });
            }
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void salvar() {
        try {
            prateleiraController.cadastrarPrateleira(txtLocal.getText(), txtDescricaoGrupo.getText());
            JOptionPane.showMessageDialog(this, "Prateleira cadastrada com sucesso!");
            limparCampos();
            carregarPrateleiras();
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void atualizar() {
        try {
            Long id = obterIdCampo();
            if (id == null) {
                mostrarAviso("Selecione uma prateleira para atualizar.");
                return;
            }

            Prateleira prateleira = new Prateleira();
            prateleira.setId(id);
            prateleira.setLocalPrateleira(txtLocal.getText());
            prateleira.setDescricaoGrupo(txtDescricaoGrupo.getText());

            prateleiraController.atualizarPrateleira(prateleira);
            JOptionPane.showMessageDialog(this, "Prateleira atualizada com sucesso!");
            limparCampos();
            carregarPrateleiras();
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void excluir() {
        try {
            Long id = obterIdCampo();
            if (id == null) {
                mostrarAviso("Selecione uma prateleira para excluir.");
                return;
            }

            int opcao = JOptionPane.showConfirmDialog(this, "Deseja excluir a prateleira selecionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                prateleiraController.excluirPrateleira(id);
                JOptionPane.showMessageDialog(this, "Prateleira excluída com sucesso!");
                limparCampos();
                carregarPrateleiras();
            }
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void preencherSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || prateleiras == null) {
            return;
        }

        Prateleira prateleira = prateleiras.get(tabela.convertRowIndexToModel(linha));
        txtId.setText(String.valueOf(prateleira.getId()));
        txtLocal.setText(prateleira.getLocalPrateleira());
        txtDescricaoGrupo.setText(prateleira.getDescricaoGrupo());
    }

    private Long obterIdCampo() {
        if (txtId.getText() == null || txtId.getText().isBlank()) {
            return null;
        }
        return Long.parseLong(txtId.getText());
    }

    private void limparCampos() {
        txtId.setText("");
        txtLocal.setText("");
        txtDescricaoGrupo.setText("");
        tabela.clearSelection();
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }
}
