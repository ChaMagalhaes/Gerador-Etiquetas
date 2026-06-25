package dev.charles.gerador_etiquetas.bo;

import dev.charles.gerador_etiquetas.dao.EtiquetaDAO;
import dev.charles.gerador_etiquetas.dao.PrateleiraDAO;
import dev.charles.gerador_etiquetas.model.Etiqueta;
import dev.charles.gerador_etiquetas.model.EtiquetaCodigoOriginal;
import dev.charles.gerador_etiquetas.model.Fabricante;
import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.model.Prateleira;
import dev.charles.gerador_etiquetas.model.SubGrupo;
import dev.charles.gerador_etiquetas.patterns.composite.CardEtiqueta;
import dev.charles.gerador_etiquetas.patterns.composite.PaginaA4Etiqueta;
import dev.charles.gerador_etiquetas.patterns.strategy.EtiquetaHtmlStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EtiquetaBO {

    private final EtiquetaDAO etiquetaDAO;
    private final PrateleiraDAO prateleiraDAO;
    private final LogAcaoBO logAcaoBO;

    public EtiquetaBO() {
        this.etiquetaDAO = new EtiquetaDAO();
        this.prateleiraDAO = new PrateleiraDAO();
        this.logAcaoBO = new LogAcaoBO();
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
        prateleira.setDescricaoGrupo(descricaoGrupo != null ? descricaoGrupo.trim() : null);
        prateleira = prateleiraDAO.salvar(prateleira);

        cadastrarEtiqueta(
                descricao,
                prateleira,
                null,
                null,
                null,
                codigoVenda,
                codigosOriginais,
                larguraCm,
                alturaCm
        );
    }

    public Etiqueta cadastrarEtiqueta(
            String descricao,
            Prateleira prateleira,
            Grupo grupo,
            SubGrupo subGrupo,
            Fabricante fabricante,
            String codigoVenda,
            List<String> codigosOriginais,
            double larguraCm,
            double alturaCm
    ) {
        validarDados(descricao, prateleira, codigoVenda, larguraCm, alturaCm);

        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setPrateleira(prateleira);
        etiqueta.setGrupo(grupo);
        etiqueta.setSubGrupo(subGrupo);
        etiqueta.setDescricao(descricao.trim());
        etiqueta.setCodigoVenda(codigoVenda.trim());
        etiqueta.setCodigosOriginais(converterStringsParaCodigosOriginais(codigosOriginais, fabricante));
        etiqueta.setDataCriacao(LocalDateTime.now());
        etiqueta.setLarguraCm(larguraCm);
        etiqueta.setAlturaCm(alturaCm);

        Etiqueta etiquetaSalva = etiquetaDAO.salvar(etiqueta);

        logAcaoBO.registrar(
                "CADASTRAR",
                "ETIQUETA",
                etiquetaSalva.getId(),
                "Cadastrou etiqueta: " + etiquetaSalva.getDescricao()
        );

        return etiquetaSalva;
    }

    public void atualizarEtiqueta(Etiqueta etiqueta) {
        if (etiqueta == null || etiqueta.getId() == null) {
            throw new RuntimeException("Etiqueta inválida para atualização.");
        }

        validarDados(
                etiqueta.getDescricao(),
                etiqueta.getPrateleira(),
                etiqueta.getCodigoVenda(),
                etiqueta.getLarguraCm(),
                etiqueta.getAlturaCm()
        );

        etiqueta.setDescricao(etiqueta.getDescricao().trim());
        etiqueta.setCodigoVenda(etiqueta.getCodigoVenda().trim());
        limparCodigosOriginais(etiqueta.getCodigosOriginais());

        etiquetaDAO.atualizar(etiqueta);

        logAcaoBO.registrar(
                "ALTERAR",
                "ETIQUETA",
                etiqueta.getId(),
                "Alterou etiqueta: " + etiqueta.getDescricao()
        );
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

        Etiqueta etiqueta = etiquetaDAO.buscarPorId(id);

        etiquetaDAO.excluir(id);

        String descricao = etiqueta != null && etiqueta.getDescricao() != null
                ? etiqueta.getDescricao()
                : "ID " + id;

        logAcaoBO.registrar(
                "EXCLUIR",
                "ETIQUETA",
                id,
                "Excluiu etiqueta: " + descricao
        );
    }

    public String gerarPaginaA4Html(List<Etiqueta> etiquetas, EtiquetaHtmlStrategy strategy) {
        if (etiquetas == null || etiquetas.isEmpty()) {
            throw new RuntimeException("Nenhuma etiqueta selecionada para impressão.");
        }

        if (strategy == null) {
            throw new RuntimeException("Estratégia de geração HTML é obrigatória.");
        }

        PaginaA4Etiqueta pagina = new PaginaA4Etiqueta();

        for (Etiqueta etiqueta : etiquetas) {
            CardEtiqueta card = new CardEtiqueta(etiqueta, strategy);
            pagina.adicionar(card);
        }

        String html = pagina.renderizar();

        logAcaoBO.registrar(
                "IMPRIMIR",
                "ETIQUETA",
                null,
                "Gerou impressão de " + etiquetas.size() + " etiqueta(s)"
        );

        return html;
    }

    public List<EtiquetaCodigoOriginal> converterStringsParaCodigosOriginais(List<String> codigosOriginais, Fabricante fabricante) {
        List<EtiquetaCodigoOriginal> listaConvertida = new ArrayList<>();

        if (codigosOriginais == null) {
            return listaConvertida;
        }

        for (String codigoTexto : codigosOriginais) {
            if (codigoTexto == null || codigoTexto.isBlank()) {
                continue;
            }

            EtiquetaCodigoOriginal codigoOriginal = new EtiquetaCodigoOriginal();
            codigoOriginal.setCodigoOriginal(codigoTexto.trim());
            codigoOriginal.setFabricante(fabricante);

            listaConvertida.add(codigoOriginal);
        }

        return listaConvertida;
    }

    private void limparCodigosOriginais(List<EtiquetaCodigoOriginal> codigosOriginais) {
        if (codigosOriginais == null) {
            return;
        }

        codigosOriginais.removeIf(codigo ->
                codigo == null ||
                        codigo.getCodigoOriginal() == null ||
                        codigo.getCodigoOriginal().isBlank()
        );

        for (EtiquetaCodigoOriginal codigo : codigosOriginais) {
            codigo.setCodigoOriginal(codigo.getCodigoOriginal().trim());
        }
    }

    private void validarDados(
            String descricao,
            String localPrateleira,
            String codigoVenda,
            double larguraCm,
            double alturaCm
    ) {
        if (localPrateleira == null || localPrateleira.isBlank()) {
            throw new RuntimeException("O endereço da prateleira é obrigatório.");
        }

        validarDadosComuns(descricao, codigoVenda, larguraCm, alturaCm);
    }

    private void validarDados(
            String descricao,
            Prateleira prateleira,
            String codigoVenda,
            double larguraCm,
            double alturaCm
    ) {
        if (prateleira == null || prateleira.getId() == null) {
            throw new RuntimeException("A prateleira da etiqueta é obrigatória.");
        }

        validarDadosComuns(descricao, codigoVenda, larguraCm, alturaCm);
    }

    private void validarDadosComuns(String descricao, String codigoVenda, double larguraCm, double alturaCm) {
        if (descricao == null || descricao.isBlank()) {
            throw new RuntimeException("A descrição da etiqueta é obrigatória.");
        }

        if (codigoVenda == null || codigoVenda.isBlank()) {
            throw new RuntimeException("O código de venda é obrigatório.");
        }

        if (larguraCm <= 0 || alturaCm <= 0) {
            throw new RuntimeException("Largura e altura precisam ser maiores que zero.");
        }
    }
}