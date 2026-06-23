package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.EtiquetaBO;
import dev.charles.gerador_etiquetas.model.Etiqueta;

import java.util.List;

public class EtiquetaController {

    private EtiquetaBO etiquetaBO;

    public EtiquetaController() {
        this.etiquetaBO = new dev.charles.gerador_etiquetas.bo.EtiquetaBO();
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

    public List<Etiqueta> listarEtiquetas() {
        return etiquetaBO.listarEtiquetas();
    }

    public Etiqueta buscarEtiquetaPorId(Long id) {
        return etiquetaBO.buscarEtiquetaPorId(id);
    }

    public void excluirEtiqueta(Long id) {
        etiquetaBO.excluirEtiqueta(id);
    }
}