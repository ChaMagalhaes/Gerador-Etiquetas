package dev.charles.gerador_etiquetas.patterns.strategy;

import dev.charles.gerador_etiquetas.model.Etiqueta;

public interface EtiquetaHtmlStrategy {
    String gerarHtml(Etiqueta etiqueta);
}
