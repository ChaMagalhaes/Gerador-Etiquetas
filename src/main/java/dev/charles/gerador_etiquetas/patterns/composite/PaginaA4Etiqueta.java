package dev.charles.gerador_etiquetas.patterns.composite;

import java.util.ArrayList;
import java.util.List;

public class PaginaA4Etiqueta implements ComponenteHtml {

    private final List<ComponenteHtml> componentes;

    public PaginaA4Etiqueta() {
        this.componentes = new ArrayList<>();
    }

    public void adicionar(ComponenteHtml componente) {
        componentes.add(componente);
    }

    public void remover(ComponenteHtml componente) {
        componentes.remove(componente);
    }

    @Override
    public String renderizar() {
        StringBuilder html = new StringBuilder();

        html.append("""
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <title>Etiquetas</title>

                    <style>
                        @page {
                            size: A4;
                            margin: 0;
                        }

                        * {
                            box-sizing: border-box;
                        }

                        body {
                            margin: 0;
                            padding: 0;
                            font-family: Arial, Helvetica, sans-serif;
                            color: #000;
                            background: #eaeaea;
                        }

                        .pagina-a4 {
                            width: 210mm;
                            min-height: 297mm;
                            margin: 0 auto;
                            padding: 10mm;
                            background: #fff;

                            display: flex;
                            flex-wrap: wrap;
                            align-content: flex-start;
                            align-items: flex-start;
                            gap: 3mm;
                        }

                        .etiqueta-card {
                            flex: 0 0 auto;
                            border: 0.35mm solid #000;
                            padding: 4mm;
                            background: #fff;

                            display: flex;
                            flex-direction: column;
                            justify-content: space-between;
                            gap: 2mm;

                            overflow: hidden;
                            break-inside: avoid;
                            page-break-inside: avoid;
                            -webkit-print-color-adjust: exact;
                            print-color-adjust: exact;
                        }

                        .etiqueta-card * {
                            min-width: 0;
                        }

                        .descricao {
                            font-size: var(--fonte-descricao);
                            font-weight: 500;
                            line-height: 1.15;
                            text-transform: uppercase;
                            overflow-wrap: anywhere;
                            word-break: normal;
                        }

                        .corpo-etiqueta {
                            display: flex;
                            flex-direction: column;
                            gap: 1.8mm;
                        }

                        .corpo-etiqueta--simples {
                            gap: 2.4mm;
                        }

                        .prateleira {
                            font-size: var(--fonte-prateleira);
                            font-weight: 900;
                            line-height: 1.05;
                            overflow-wrap: anywhere;
                            word-break: normal;
                        }

                        .rodape-etiqueta {
                            display: flex;
                            align-items: flex-end;
                            justify-content: space-between;
                            gap: 2.5mm;
                        }

                        .codigo-venda {
                            flex: 1 1 auto;
                            font-size: var(--fonte-codigo-venda);
                            font-weight: 600;
                            line-height: 1.15;
                            white-space: normal;
                            overflow-wrap: anywhere;
                            word-break: normal;
                        }

                        .codigos-originais {
                            flex: 0 1 48%;
                            display: flex;
                            flex-direction: column;
                            align-items: flex-end;
                            gap: 0.6mm;
                            text-align: right;
                        }

                        .codigo-original {
                            max-width: 100%;
                            font-size: var(--fonte-codigo-original);
                            font-weight: 500;
                            line-height: 1.15;
                            white-space: normal;
                            overflow-wrap: anywhere;
                            word-break: normal;
                        }

                        @media print {
                            body {
                                background: #fff;
                            }

                            .pagina-a4 {
                                width: 210mm;
                                min-height: 297mm;
                                margin: 0;
                                padding: 10mm;
                            }
                        }
                    </style>
                </head>
                <body>
                    <main class="pagina-a4">
                """);

        for (ComponenteHtml componente : componentes) {
            html.append(componente.renderizar());
        }

        html.append("""
                    </main>
                </body>
                </html>
                """);

        return html.toString();
    }
}
