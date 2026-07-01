package dev.charles.gerador_etiquetas.patterns.strategy;

import dev.charles.gerador_etiquetas.model.Etiqueta;

public class EtiquetaCompletaHtmlStrategy extends BaseEtiquetaHtmlStrategy {

    @Override
    public String gerarHtml(Etiqueta etiqueta) {
        String codigosOriginaisHtml = gerarCodigosOriginaisHtml(
                etiqueta != null ? etiqueta.getCodigosOriginais() : null
        );

        return """
                <div class="etiqueta-card etiqueta-card--completa" style="%s">
                    <div class="descricao">%s</div>

                    <div class="corpo-etiqueta">
                        <div class="prateleira">%s</div>

                        <div class="rodape-etiqueta">
                            <div class="codigo-venda">%s</div>

                            <div class="codigos-originais">
                                %s
                            </div>
                        </div>
                    </div>
                </div>
                """.formatted(
                montarEstiloEtiqueta(etiqueta),
                escaparHtml(descricaoDaEtiqueta(etiqueta)),
                escaparHtml(localPrateleira(etiqueta)),
                escaparHtml(codigoVenda(etiqueta)),
                codigosOriginaisHtml
        );
    }
}
