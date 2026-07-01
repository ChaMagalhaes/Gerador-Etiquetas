package dev.charles.gerador_etiquetas.patterns.strategy;

import dev.charles.gerador_etiquetas.model.Etiqueta;

public class EtiquetaSimplesHtmlStrategy extends BaseEtiquetaHtmlStrategy {

    @Override
    public String gerarHtml(Etiqueta etiqueta) {
        return """
                <div class="etiqueta-card etiqueta-card--simples" style="%s">
                    <div class="descricao">%s</div>

                    <div class="corpo-etiqueta corpo-etiqueta--simples">
                        <div class="prateleira">%s</div>
                        <div class="codigo-venda">%s</div>
                    </div>
                </div>
                """.formatted(
                montarEstiloEtiqueta(etiqueta),
                escaparHtml(descricaoDaEtiqueta(etiqueta)),
                escaparHtml(localPrateleira(etiqueta)),
                escaparHtml(codigoVenda(etiqueta))
        );
    }
}
