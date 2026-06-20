package dev.charles.gerador_etiquetas.patterns.strategy;

import dev.charles.gerador_etiquetas.model.Etiqueta;

public class EtiquetaSimplesHtmlStrategy implements EtiquetaHtmlStrategy{
    @Override
    public String gerarHtml(Etiqueta etiqueta) {
        return """
                <div class="etiqueta-card">
                    <h3>%s</h3>
                    <p><strong>Cód. Venda:</strong> %s</p>
                    <p><strong>Prateleira:</strong> %s</p>
                </div>
                """.formatted(
                etiqueta.getDescricao(),
                etiqueta.getCodigoVenda(),
                etiqueta.getPrateleira().getLocalPrateleira()
        );
    }
}
