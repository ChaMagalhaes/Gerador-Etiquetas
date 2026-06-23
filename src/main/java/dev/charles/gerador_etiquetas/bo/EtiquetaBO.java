package dev.charles.gerador_etiquetas.bo;

import dev.charles.gerador_etiquetas.dao.EtiquetaDAO;
import dev.charles.gerador_etiquetas.dao.PrateleiraDAO;
import dev.charles.gerador_etiquetas.model.Etiqueta;
import dev.charles.gerador_etiquetas.model.Prateleira;
import dev.charles.gerador_etiquetas.patterns.composite.CardEtiqueta;
import dev.charles.gerador_etiquetas.patterns.composite.PaginaA4Etiqueta;
import dev.charles.gerador_etiquetas.patterns.strategy.EtiquetaHtmlStrategy;

import java.time.LocalDateTime;
import java.util.List;

public class EtiquetaBO {

    private EtiquetaDAO etiquetaDAO;
    private PrateleiraDAO prateleiraDAO;

    public EtiquetaBO() {
        this.etiquetaDAO = new EtiquetaDAO();
        this.prateleiraDAO = new PrateleiraDAO();
    }

    public void cadastrarEtiqueta(
            String descricao,
            String localPrateleira,
            String descricaoGrupo,
            String codigoVenda,
            List<String> codigosOriginais,
            double larguraCm,
            double alturaCm
    ) {
        validarDados(descricao, localPrateleira, codigoVenda, larguraCm, alturaCm);

        Prateleira prateleira = new Prateleira();
        prateleira.setLocalPrateleira(localPrateleira.trim());
        prateleira.setDescricaoGrupo(descricaoGrupo);

        prateleira = prateleiraDAO.salvar(prateleira);

        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setPrateleira(prateleira);
        etiqueta.setDescricao(descricao.trim());
        etiqueta.setCodigoVenda(codigoVenda.trim());
        etiqueta.setCodigosOriginais(codigosOriginais);
        etiqueta.setDataCriacao(LocalDateTime.now());
        etiqueta.setLarguraCm(larguraCm);
        etiqueta.setAlturaCm(alturaCm);

        etiquetaDAO.salvar(etiqueta);
    }

    public void atualizarEtiqueta(Etiqueta etiqueta) {
        if (etiqueta == null || etiqueta.getId() == null) {
            throw new RuntimeException("Etiqueta inválida para atualização.");
        }

        if (etiqueta.getPrateleira() == null || etiqueta.getPrateleira().getId() == null) {
            throw new RuntimeException("A prateleira da etiqueta é obrigatória.");
        }

        validarDados(
                etiqueta.getDescricao(),
                etiqueta.getPrateleira().getLocalPrateleira(),
                etiqueta.getCodigoVenda(),
                etiqueta.getLarguraCm(),
                etiqueta.getAlturaCm()
        );

        etiquetaDAO.atualizar(etiqueta);
    }

    public List<Etiqueta> listarEtiquetas() {
        return etiquetaDAO.listar();
    }

    public Etiqueta buscarEtiquetaPorId(Long id) {
        if (id == null) {
            throw new RuntimeException("ID da etiqueta é obrigatório.");
        }

        return etiquetaDAO.buscarPorId(id);
    }

    public List<Etiqueta> pesquisarEtiquetasPorTipo(String tipoBusca, String termo) {
        if (termo == null || termo.isBlank()) {
            return etiquetaDAO.listar();
        }

        if (tipoBusca == null || tipoBusca.isBlank()) {
            throw new RuntimeException("Selecione o tipo de busca.");
        }

        return etiquetaDAO.pesquisarPorTipo(tipoBusca, termo.trim());
    }

    public void excluirEtiqueta(Long id) {
        if (id == null) {
            throw new RuntimeException("ID da etiqueta é obrigatório.");
        }

        etiquetaDAO.excluir(id);
    }

    public String gerarPaginaA4Html(List<Etiqueta> etiquetas, EtiquetaHtmlStrategy strategy) {
        if (etiquetas == null || etiquetas.isEmpty()) {
            throw new RuntimeException("Nenhuma etiqueta selecionada para impressão.");
        }

        PaginaA4Etiqueta pagina = new PaginaA4Etiqueta();

        for (Etiqueta etiqueta : etiquetas) {
            CardEtiqueta card = new CardEtiqueta(etiqueta, strategy);
            pagina.adicionar(card);
        }

        return pagina.renderizar();
    }

    private void validarDados(
            String descricao,
            String localPrateleira,
            String codigoVenda,
            double larguraCm,
            double alturaCm
    ) {
        if (descricao == null || descricao.isBlank()) {
            throw new RuntimeException("A descrição da etiqueta é obrigatória.");
        }

        if (localPrateleira == null || localPrateleira.isBlank()) {
            throw new RuntimeException("O endereço da prateleira é obrigatório.");
        }

        if (codigoVenda == null || codigoVenda.isBlank()) {
            throw new RuntimeException("O código de venda é obrigatório.");
        }

        if (larguraCm <= 0 || alturaCm <= 0) {
            throw new RuntimeException("Largura e altura precisam ser maiores que zero.");
        }
    }
}