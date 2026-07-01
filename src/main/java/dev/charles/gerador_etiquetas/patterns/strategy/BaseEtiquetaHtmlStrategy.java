package dev.charles.gerador_etiquetas.patterns.strategy;

import dev.charles.gerador_etiquetas.model.Etiqueta;
import dev.charles.gerador_etiquetas.model.EtiquetaCodigoOriginal;

import java.util.List;
import java.util.Locale;

public abstract class BaseEtiquetaHtmlStrategy implements EtiquetaHtmlStrategy {

    private static final double LARGURA_PADRAO_CM = 12.0;
    private static final double ALTURA_PADRAO_CM = 6.0;

    protected String montarEstiloEtiqueta(Etiqueta etiqueta) {
        double largura = larguraDaEtiqueta(etiqueta);
        double altura = alturaDaEtiqueta(etiqueta);

        double fonteDescricao = calcularFonteDescricao(largura, altura, descricaoDaEtiqueta(etiqueta));
        double fontePrateleira = calcularFontePrateleira(largura, altura, localPrateleira(etiqueta));
        double fonteCodigoVenda = calcularFonteCodigoVenda(largura, altura, codigoVenda(etiqueta));
        double fonteCodigoOriginal = calcularFonteCodigoOriginal(largura, altura, etiqueta != null ? etiqueta.getCodigosOriginais() : null);

        return """
                width: %scm;
                height: %scm;
                --fonte-descricao: %spx;
                --fonte-prateleira: %spx;
                --fonte-codigo-venda: %spx;
                --fonte-codigo-original: %spx;
                """.formatted(
                formatarNumero(largura),
                formatarNumero(altura),
                formatarNumero(fonteDescricao),
                formatarNumero(fontePrateleira),
                formatarNumero(fonteCodigoVenda),
                formatarNumero(fonteCodigoOriginal)
        );
    }

    protected String gerarCodigosOriginaisHtml(List<EtiquetaCodigoOriginal> codigosOriginais) {
        if (codigosOriginais == null || codigosOriginais.isEmpty()) {
            return "<div class=\"codigo-original\">Sem código original</div>";
        }

        StringBuilder html = new StringBuilder();

        for (EtiquetaCodigoOriginal codigo : codigosOriginais) {
            if (codigo == null || codigo.toString() == null || codigo.toString().isBlank()) {
                continue;
            }

            html.append("""
                    <div class="codigo-original">%s</div>
                    """.formatted(escaparHtml(codigo.toString())));
        }

        if (html.isEmpty()) {
            return "<div class=\"codigo-original\">Sem código original</div>";
        }

        return html.toString();
    }

    protected String descricaoDaEtiqueta(Etiqueta etiqueta) {
        if (etiqueta == null) {
            return "";
        }
        return textoOuVazio(etiqueta.getDescricao());
    }

    protected String codigoVenda(Etiqueta etiqueta) {
        if (etiqueta == null) {
            return "";
        }
        return textoOuVazio(etiqueta.getCodigoVenda());
    }

    protected String localPrateleira(Etiqueta etiqueta) {
        if (etiqueta == null || etiqueta.getPrateleira() == null) {
            return "Sem prateleira";
        }
        return textoOuVazio(etiqueta.getPrateleira().getLocalPrateleira());
    }

    protected String escaparHtml(String texto) {
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

    private double larguraDaEtiqueta(Etiqueta etiqueta) {
        if (etiqueta == null || etiqueta.getLarguraCm() <= 0) {
            return LARGURA_PADRAO_CM;
        }
        return etiqueta.getLarguraCm();
    }

    private double alturaDaEtiqueta(Etiqueta etiqueta) {
        if (etiqueta == null || etiqueta.getAlturaCm() <= 0) {
            return ALTURA_PADRAO_CM;
        }
        return etiqueta.getAlturaCm();
    }

    private double calcularFonteDescricao(double larguraCm, double alturaCm, String descricao) {
        double menorMedida = Math.min(larguraCm, alturaCm);
        double fonte = menorMedida * 2.7;

        int tamanhoTexto = tamanho(descricao);
        if (tamanhoTexto > 28) {
            fonte -= (tamanhoTexto - 28) * 0.10;
        }

        return limitar(fonte, 8, 16);
    }

    private double calcularFontePrateleira(double larguraCm, double alturaCm, String prateleira) {
        double menorMedida = Math.min(larguraCm, alturaCm);
        double fonte = menorMedida * 3.4;

        int tamanhoTexto = tamanho(prateleira);
        if (tamanhoTexto > 20) {
            fonte -= (tamanhoTexto - 20) * 0.25;
        }

        return limitar(fonte, 12, 22);
    }

    private double calcularFonteCodigoVenda(double larguraCm, double alturaCm, String codigoVenda) {
        double menorMedida = Math.min(larguraCm, alturaCm);
        double fonte = menorMedida * 2.5;

        int tamanhoTexto = tamanho(codigoVenda);
        if (tamanhoTexto > 16) {
            fonte -= (tamanhoTexto - 16) * 0.15;
        }

        return limitar(fonte, 8, 14);
    }

    private double calcularFonteCodigoOriginal(double larguraCm, double alturaCm, List<EtiquetaCodigoOriginal> codigosOriginais) {
        double menorMedida = Math.min(larguraCm, alturaCm);
        double fonte = menorMedida * 2.35;

        int quantidade = codigosOriginais != null ? codigosOriginais.size() : 0;
        int maiorCodigo = maiorTextoCodigoOriginal(codigosOriginais);

        if (quantidade > 2) {
            fonte -= (quantidade - 2) * 0.85;
        }

        if (maiorCodigo > 18) {
            fonte -= (maiorCodigo - 18) * 0.10;
        }

        return limitar(fonte, 7, 12);
    }

    private int maiorTextoCodigoOriginal(List<EtiquetaCodigoOriginal> codigosOriginais) {
        if (codigosOriginais == null || codigosOriginais.isEmpty()) {
            return 0;
        }

        int maior = 0;
        for (EtiquetaCodigoOriginal codigo : codigosOriginais) {
            if (codigo != null && codigo.toString() != null) {
                maior = Math.max(maior, codigo.toString().length());
            }
        }

        return maior;
    }

    private int tamanho(String texto) {
        return texto != null ? texto.length() : 0;
    }

    private String textoOuVazio(String texto) {
        return texto != null ? texto.trim() : "";
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
}
