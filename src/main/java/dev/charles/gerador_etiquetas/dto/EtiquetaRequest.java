package dev.charles.gerador_etiquetas.dto;

import java.util.List;

public record EtiquetaRequest(
        String descricao,
        Long prateleiraId,
        Long grupoId,
        Long subGrupoId,
        Long fabricanteId,
        String codigoVenda,
        List<String> codigosOriginais,
        double larguraCm,
        double alturaCm
) {
}
