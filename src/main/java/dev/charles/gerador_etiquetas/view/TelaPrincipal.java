package dev.charles.gerador_etiquetas.view;

import dev.charles.gerador_etiquetas.bo.EtiquetaBO;
import dev.charles.gerador_etiquetas.model.Etiqueta;
import dev.charles.gerador_etiquetas.model.Usuario;
import dev.charles.gerador_etiquetas.patterns.strategy.EtiquetaCompletaHtmlStrategy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private Usuario usuarioLogado;
    private final EtiquetaBO etiquetaBO;

    private JComboBox<String> comboTipoBusca;
    private JTextField txtBusca;

    private JTable tabelaEtiquetas;
    private DefaultTableModel modeloTabela;

    private DefaultListModel<Etiqueta> modeloListaImpressao;
    private JList<Etiqueta> listaImpressao;

    private List<Etiqueta> etiquetasCadastradas;
    private List<Etiqueta> etiquetasSelecionadas;

    public TelaPrincipal(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.etiquetaBO = new EtiquetaBO();
        this.etiquetasCadastradas = new ArrayList<>();
        this.etiquetasSelecionadas = new ArrayList<>();

        setTitle("Gerador de Etiquetas - Usuário: " + usuarioLogado.getNome());
        setSize(1150, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        criarComponentes();
        carregarEtiquetas();
    }

    private void criarComponentes() {
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Etiquetas cadastradas", criarPainelEtiquetasCadastradas());
        abas.addTab("Etiquetas para impressão", criarPainelEtiquetasParaImpressao());
        add(abas, BorderLayout.CENTER);
    }

    private JPanel criarPainelEtiquetasCadastradas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colunas = {
                "ID",
                "Descrição",
                "Prateleira",
                "Código venda",
                "Códigos originais",
                "Tamanho"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaEtiquetas = new JTable(modeloTabela);
        tabelaEtiquetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaEtiquetas.setRowHeight(25);

        tabelaEtiquetas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirTelaEtiquetaSelecionada();
                }
            }
        });

        JScrollPane scrollTabela = new JScrollPane(tabelaEtiquetas);

        JPanel painelTopo = new JPanel(new BorderLayout(10, 10));

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));

        comboTipoBusca = new JComboBox<>();
        comboTipoBusca.addItem("Descrição");
        comboTipoBusca.addItem("Código de venda");
        comboTipoBusca.addItem("Código original");

        txtBusca = new JTextField(30);

        JButton btnPesquisar = new JButton("Pesquisar");
        JButton btnLimparBusca = new JButton("Limpar");

        painelBusca.add(new JLabel("Buscar por:"));
        painelBusca.add(comboTipoBusca);
        painelBusca.add(txtBusca);
        painelBusca.add(btnPesquisar);
        painelBusca.add(btnLimparBusca);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnNovo = new JButton("Nova etiqueta");
        JButton btnAlterar = new JButton("Abrir/Alterar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAdicionarImpressao = new JButton("Adicionar à impressão");

        btnPesquisar.addActionListener(e -> pesquisarEtiquetas());
        txtBusca.addActionListener(e -> pesquisarEtiquetas());

        btnLimparBusca.addActionListener(e -> {
            txtBusca.setText("");
            carregarEtiquetas();
        });

        btnAtualizar.addActionListener(e -> carregarEtiquetas());
        btnNovo.addActionListener(e -> abrirTelaNovaEtiqueta());
        btnAlterar.addActionListener(e -> abrirTelaEtiquetaSelecionada());
        btnExcluir.addActionListener(e -> excluirEtiquetaSelecionada());
        btnAdicionarImpressao.addActionListener(e -> adicionarEtiquetaParaImpressao());

        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnAdicionarImpressao);

        painelTopo.add(painelBusca, BorderLayout.NORTH);
        painelTopo.add(painelBotoes, BorderLayout.CENTER);

        painel.add(painelTopo, BorderLayout.NORTH);
        painel.add(scrollTabela, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelEtiquetasParaImpressao() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloListaImpressao = new DefaultListModel<>();
        listaImpressao = new JList<>(modeloListaImpressao);

        listaImpressao.setCellRenderer((list, etiqueta, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            String texto = etiqueta.getDescricao()
                    + " | "
                    + etiqueta.getPrateleira().getLocalPrateleira()
                    + " | "
                    + etiqueta.getCodigoVenda();

            label.setText(texto);
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

            if (isSelected) {
                label.setBackground(new Color(180, 210, 255));
            } else {
                label.setBackground(Color.WHITE);
            }

            return label;
        });

        listaImpressao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    removerEtiquetaSelecionadaDaImpressao();
                }
            }
        });

        JScrollPane scrollLista = new JScrollPane(listaImpressao);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnRemover = new JButton("Remover selecionada");
        JButton btnLimpar = new JButton("Limpar lista");
        JButton btnImprimir = new JButton("Imprimir / Gerar HTML");

        btnRemover.addActionListener(e -> removerEtiquetaSelecionadaDaImpressao());
        btnLimpar.addActionListener(e -> limparListaImpressao());
        btnImprimir.addActionListener(e -> imprimirEtiquetasSelecionadas());

        painelBotoes.add(btnRemover);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnImprimir);

        JLabel aviso = new JLabel("Dica: clique duas vezes em uma etiqueta para remover da lista.");
        aviso.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        painel.add(painelBotoes, BorderLayout.NORTH);
        painel.add(scrollLista, BorderLayout.CENTER);
        painel.add(aviso, BorderLayout.SOUTH);

        return painel;
    }

    private void carregarEtiquetas() {
        try {
            etiquetasCadastradas = etiquetaBO.listarEtiquetas();
            preencherTabela(etiquetasCadastradas);
        } catch (RuntimeException e) {
            mostrarErro("Erro ao carregar etiquetas: " + e.getMessage());
        }
    }

    private void pesquisarEtiquetas() {
        try {
            String termo = txtBusca.getText().trim();
            String opcaoSelecionada = comboTipoBusca.getSelectedItem().toString();
            String tipoBusca = converterTipoBusca(opcaoSelecionada);

            etiquetasCadastradas = etiquetaBO.pesquisarEtiquetasPorTipo(tipoBusca, termo);
            preencherTabela(etiquetasCadastradas);
        } catch (RuntimeException e) {
            mostrarErro("Erro ao pesquisar etiquetas: " + e.getMessage());
        }
    }

    private void preencherTabela(List<Etiqueta> etiquetas) {
        modeloTabela.setRowCount(0);

        for (Etiqueta etiqueta : etiquetas) {
            modeloTabela.addRow(new Object[]{
                    etiqueta.getId(),
                    etiqueta.getDescricao(),
                    etiqueta.getPrateleira().getLocalPrateleira(),
                    etiqueta.getCodigoVenda(),
                    formatarCodigosOriginais(etiqueta),
                    etiqueta.getLarguraCm() + " x " + etiqueta.getAlturaCm() + " cm"
            });
        }
    }

    private String converterTipoBusca(String opcaoSelecionada) {
        return switch (opcaoSelecionada) {
            case "Descrição" -> "DESCRICAO";
            case "Código de venda" -> "CODIGO_VENDA";
            case "Código original" -> "CODIGO_ORIGINAL";
            default -> throw new RuntimeException("Opção de busca inválida.");
        };
    }

    private void abrirTelaNovaEtiqueta() {
        TelaFormularioEtiqueta tela = new TelaFormularioEtiqueta(this, this::carregarEtiquetas);
        tela.setVisible(true);
    }

    private void abrirTelaEtiquetaSelecionada() {
        Etiqueta etiqueta = obterEtiquetaSelecionada();

        if (etiqueta == null) {
            return;
        }

        TelaDetalheEtiqueta telaDetalhe = new TelaDetalheEtiqueta(this, etiqueta, this::carregarEtiquetas);
        telaDetalhe.setVisible(true);
    }

    private void excluirEtiquetaSelecionada() {
        Etiqueta etiqueta = obterEtiquetaSelecionada();

        if (etiqueta == null) {
            return;
        }

        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Deseja excluir a etiqueta selecionada?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            etiquetaBO.excluirEtiqueta(etiqueta.getId());
            JOptionPane.showMessageDialog(this, "Etiqueta excluída com sucesso!");
            carregarEtiquetas();
        }
    }

    private Etiqueta obterEtiquetaSelecionada() {
        int linhaSelecionada = tabelaEtiquetas.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma etiqueta.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int linhaModelo = tabelaEtiquetas.convertRowIndexToModel(linhaSelecionada);
        return etiquetasCadastradas.get(linhaModelo);
    }

    private void adicionarEtiquetaParaImpressao() {
        Etiqueta etiqueta = obterEtiquetaSelecionada();

        if (etiqueta == null) {
            return;
        }

        if (jaFoiSelecionada(etiqueta)) {
            JOptionPane.showMessageDialog(this, "Essa etiqueta já está na lista de impressão.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        etiquetasSelecionadas.add(etiqueta);
        modeloListaImpressao.addElement(etiqueta);
        JOptionPane.showMessageDialog(this, "Etiqueta adicionada à impressão.");
    }

    private boolean jaFoiSelecionada(Etiqueta etiqueta) {
        for (Etiqueta selecionada : etiquetasSelecionadas) {
            if (selecionada.getId() != null && selecionada.getId().equals(etiqueta.getId())) {
                return true;
            }
        }

        return false;
    }

    private void removerEtiquetaSelecionadaDaImpressao() {
        int index = listaImpressao.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma etiqueta para remover.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        etiquetasSelecionadas.remove(index);
        modeloListaImpressao.remove(index);
    }

    private void limparListaImpressao() {
        etiquetasSelecionadas.clear();
        modeloListaImpressao.clear();
    }

    private void imprimirEtiquetasSelecionadas() {
        if (etiquetasSelecionadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos uma etiqueta na lista de impressão.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String html = etiquetaBO.gerarPaginaA4Html(etiquetasSelecionadas, new EtiquetaCompletaHtmlStrategy());

        GeradorArquivoHtml gerador = new GeradorArquivoHtml();
        gerador.salvar(html, "etiquetas.html");

        JOptionPane.showMessageDialog(this, "Arquivo HTML gerado com sucesso!");
    }

    private String formatarCodigosOriginais(Etiqueta etiqueta) {
        if (etiqueta.getCodigosOriginais() == null || etiqueta.getCodigosOriginais().isEmpty()) {
            return "";
        }

        return String.join(" / ", etiqueta.getCodigosOriginais());
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
