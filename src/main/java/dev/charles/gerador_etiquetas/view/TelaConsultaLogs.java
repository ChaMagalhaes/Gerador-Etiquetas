package dev.charles.gerador_etiquetas.view;

import dev.charles.gerador_etiquetas.controller.LogAcaoController;
import dev.charles.gerador_etiquetas.controller.UsuarioController;
import dev.charles.gerador_etiquetas.model.LogAcao;
import dev.charles.gerador_etiquetas.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaConsultaLogs extends JDialog {

    private final LogAcaoController logAcaoController;
    private final UsuarioController usuarioController;

    private JComboBox<Usuario> comboUsuario;
    private JComboBox<String> comboEntidade;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public TelaConsultaLogs(JFrame telaPai) {
        super(telaPai, "Consulta de Logs", true);

        this.logAcaoController = new LogAcaoController();
        this.usuarioController = new UsuarioController();

        setSize(1050, 600);
        setLocationRelativeTo(telaPai);
        setResizable(false);

        criarComponentes();
        carregarUsuarios();
        carregarLogsTodos();
    }

    private void criarComponentes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel titulo = new JLabel("Logs de Ação do Usuário", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel filtros = criarPainelFiltros();

        modeloTabela = new DefaultTableModel(new String[]{
                "ID",
                "Data/hora",
                "Usuário",
                "Ação",
                "Entidade",
                "ID entidade",
                "Detalhes"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Long.class;
                    default -> String.class;
                };
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(24);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setAutoCreateRowSorter(true);

        tabela.getColumnModel().getColumn(0).setMaxWidth(60);
        tabela.getColumnModel().getColumn(5).setMaxWidth(100);

        JPanel topo = new JPanel(new BorderLayout(10, 10));
        topo.add(titulo, BorderLayout.NORTH);
        topo.add(filtros, BorderLayout.SOUTH);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(painel);
    }

    private JPanel criarPainelFiltros() {
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));

        comboUsuario = new JComboBox<>();
        comboUsuario.setPreferredSize(new Dimension(220, 28));
        configurarRendererUsuario();

        comboEntidade = new JComboBox<>();
        comboEntidade.setPreferredSize(new Dimension(180, 28));

        comboEntidade.addItem("TODOS");
        comboEntidade.addItem("ETIQUETA");
        comboEntidade.addItem("USUARIO");
        comboEntidade.addItem("PRATELEIRA");
        comboEntidade.addItem("GRUPO");
        comboEntidade.addItem("SUBGRUPO");
        comboEntidade.addItem("FABRICANTE");
        comboEntidade.addItem("CODIGO_ORIGINAL");
        comboEntidade.addItem("SISTEMA");

        JButton btnTodos = new JButton("Todos");
        JButton btnPorUsuario = new JButton("Filtrar usuário");
        JButton btnPorEntidade = new JButton("Filtrar entidade");
        JButton btnFechar = new JButton("Fechar");

        btnTodos.addActionListener(e -> {
            comboUsuario.setSelectedIndex(0);
            comboEntidade.setSelectedItem("TODOS");
            carregarLogsTodos();
        });

        btnPorUsuario.addActionListener(e -> carregarLogsUsuario());
        btnPorEntidade.addActionListener(e -> carregarLogsEntidade());
        btnFechar.addActionListener(e -> dispose());

        filtros.add(new JLabel("Usuário:"));
        filtros.add(comboUsuario);
        filtros.add(btnPorUsuario);

        filtros.add(new JLabel("Entidade:"));
        filtros.add(comboEntidade);
        filtros.add(btnPorEntidade);

        filtros.add(btnTodos);
        filtros.add(btnFechar);

        return filtros;
    }

    private void carregarUsuarios() {
        try {
            comboUsuario.removeAllItems();

            comboUsuario.addItem(null);

            for (Usuario usuario : usuarioController.listarUsuarios()) {
                comboUsuario.addItem(usuario);
            }

        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void carregarLogsTodos() {
        try {
            preencherTabela(logAcaoController.listarTodos());
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void carregarLogsUsuario() {
        try {
            Usuario usuario = (Usuario) comboUsuario.getSelectedItem();

            if (usuario == null || usuario.getId() == null) {
                mostrarAviso("Selecione um usuário.");
                return;
            }

            preencherTabela(logAcaoController.listarPorUsuario(usuario.getId()));

        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void carregarLogsEntidade() {
        try {
            String entidade = comboEntidade.getSelectedItem().toString();

            if ("TODOS".equals(entidade)) {
                carregarLogsTodos();
                return;
            }

            preencherTabela(logAcaoController.listarPorEntidade(entidade));

        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void preencherTabela(List<LogAcao> logs) {
        modeloTabela.setRowCount(0);

        for (LogAcao log : logs) {
            modeloTabela.addRow(new Object[]{
                    log.getId(),
                    log.getDataHora() != null ? log.getDataHora().format(FORMATO) : "",
                    log.getUsuario() != null ? log.getUsuario().getNome() : "",
                    log.getAcao(),
                    log.getEntidade(),
                    log.getEntidadeId() != null ? log.getEntidadeId() : "-",
                    log.getDetalhes() != null ? log.getDetalhes() : ""
            });
        }
    }

    private void configurarRendererUsuario() {
        comboUsuario.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value == null) {
                    setText("Todos/Selecione");
                } else {
                    Usuario usuario = (Usuario) value;
                    setText(usuario.getNome());
                }

                return this;
            }
        });
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Atenção",
                JOptionPane.WARNING_MESSAGE
        );
    }
}