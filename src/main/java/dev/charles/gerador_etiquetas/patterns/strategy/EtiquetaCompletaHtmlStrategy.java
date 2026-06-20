package dev.charles.gerador_etiquetas.patterns.strategy;

import dev.charles.gerador_etiquetas.model.Etiqueta;

import java.util.List;
import java.util.Locale;

public class EtiquetaCompletaHtmlStrategy implements EtiquetaHtmlStrategy {

    @Override
    public String gerarHtml(Etiqueta etiqueta) {
        double largura = etiqueta.getLarguraCm();
        double altura = etiqueta.getAlturaCm();

        double fonteDescricao = calcularFonteDescricao(largura, altura);
        double fontePrateleira = calcularFontePrateleira(largura, altura);
        double fonteCodigoVenda = calcularFonteCodigoVenda(largura, altura);
        double fonteCodigoOriginal = calcularFonteCodigoOriginal(largura, altura, etiqueta.getCodigosOriginais());

        String codigosOriginaisHtml = gerarCodigosOriginaisHtml(etiqueta.getCodigosOriginais());

        return """
                <div class="etiqueta-card" style="
                    width: %scm;
                    height: %scm;
                    --fonte-descricao: %spx;
                    --fonte-prateleira: %spx;
                    --fonte-codigo-venda: %spx;
                    --fonte-codigo-original: %spx;
                ">
                    <div class="descricao">
                        %s
                    </div>

                    <div class="conteudo-etiqueta">
                        <div class="prateleira">
                            %s
                        </div>

                        <div class="codigo-venda">
                            %s
                        </div>

                        <div class="codigos-originais">
                            %s
                        </div>
                    </div>
                </div>
                """.formatted(
                formatarNumero(largura),
                formatarNumero(altura),
                formatarNumero(fonteDescricao),
                formatarNumero(fontePrateleira),
                formatarNumero(fonteCodigoVenda),
                formatarNumero(fonteCodigoOriginal),
                escaparHtml(etiqueta.getDescricao()),
                escaparHtml(etiqueta.getPrateleira().getLocalPrateleira()),
                escaparHtml(etiqueta.getCodigoVenda()),
                codigosOriginaisHtml
        );
    }

    private String gerarCodigosOriginaisHtml(List<String> codigosOriginais) {
        if (codigosOriginais == null || codigosOriginais.isEmpty()) {
            return "<div class=\"codigo-original\">Sem código original</div>";
        }

        StringBuilder html = new StringBuilder();

        for (String codigo : codigosOriginais) {
            html.append("""
                    <div class="codigo-original">%s</div>
                    """.formatted(escaparHtml(codigo)));
        }

        return html.toString();
    }

    private double calcularFonteDescricao(double larguraCm, double alturaCm) {
        double menorMedida = Math.min(larguraCm, alturaCm);
        double fonte = menorMedida * 3.0;
        return limitar(fonte, 10, 24);
    }

    private double calcularFontePrateleira(double larguraCm, double alturaCm) {
        double menorMedida = Math.min(larguraCm, alturaCm);
        double fonte = menorMedida * 3.5;
        return limitar(fonte, 16, 28);
    }

    private double calcularFonteCodigoVenda(double larguraCm, double alturaCm) {
        double menorMedida = Math.min(larguraCm, alturaCm);
        double fonte = menorMedida * 3.0;
        return limitar(fonte, 11, 26);
    }

    private double calcularFonteCodigoOriginal(double larguraCm, double alturaCm, List<String> codigosOriginais) {
        double menorMedida = Math.min(larguraCm, alturaCm);
        double fonte = menorMedida * 2.8;

        int quantidade = 0;
        if (codigosOriginais != null) {
            quantidade = codigosOriginais.size();
        }

        if (quantidade > 3) {
            fonte -= (quantidade - 3) * 1.7;
        }

        return limitar(fonte, 7, 22);
    }

    private double limitar(double valor, double minimo, double maximo) {
        if (valor < minimo) {
            return minimo;
        }

        if (valor > maximo) {
            return maximo;
        }

        return valor;
    }

    private String formatarNumero(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }

    private String escaparHtml(String texto) {
        if (texto == null) {
            return "";
        }

        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
