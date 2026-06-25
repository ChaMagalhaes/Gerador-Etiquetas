package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.EtiquetaBO;
import dev.charles.gerador_etiquetas.model.Etiqueta;
import dev.charles.gerador_etiquetas.model.Fabricante;
import dev.charles.gerador_etiquetas.model.Grupo;
import dev.charles.gerador_etiquetas.model.Prateleira;
import dev.charles.gerador_etiquetas.model.SubGrupo;

import java.util.List;

public class EtiquetaController {

    private final EtiquetaBO etiquetaBO;

    public EtiquetaController() {
        this.etiquetaBO = new EtiquetaBO();
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
        etiquetaBO.cadastrarEtiqueta(
                descricao,
                localPrateleira,
                descricaoGrupo,
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
        return etiquetaBO.cadastrarEtiqueta(
                descricao,
                prateleira,
                grupo,
                subGrupo,
                fabricante,
                codigoVenda,
                codigosOriginais,
                larguraCm,
                alturaCm
        );
    }

    public void atualizarEtiqueta(Etiqueta etiqueta) {
        etiquetaBO.atualizarEtiqueta(etiqueta);
    }

    public List<Etiqueta> listarEtiquetas() {
        return etiquetaBO.listarEtiquetas();
    }

    public Etiqueta buscarEtiquetaPorId(Long id) {
        return etiquetaBO.buscarEtiquetaPorId(id);
    }

    public List<Etiqueta> pesquisarEtiquetasPorTipo(String tipoBusca, String termo) {
        return etiquetaBO.pesquisarEtiquetasPorTipo(tipoBusca, termo);
    }

    public void excluirEtiqueta(Long id) {
        etiquetaBO.excluirEtiqueta(id);
    }
}
