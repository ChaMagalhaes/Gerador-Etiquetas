package dev.charles.gerador_etiquetas.patterns.composite;

import dev.charles.gerador_etiquetas.model.Etiqueta;
import dev.charles.gerador_etiquetas.patterns.strategy.EtiquetaHtmlStrategy;

public class CardEtiqueta implements ComponenteHtml {

    private Etiqueta etiqueta;
    private EtiquetaHtmlStrategy strategy;

    public CardEtiqueta(Etiqueta etiqueta, EtiquetaHtmlStrategy strategy) {
        this.etiqueta = etiqueta;
        this.strategy = strategy;
    }

    @Override
    public String renderizar() {
        return strategy.gerarHtml(etiqueta);
    }
}
