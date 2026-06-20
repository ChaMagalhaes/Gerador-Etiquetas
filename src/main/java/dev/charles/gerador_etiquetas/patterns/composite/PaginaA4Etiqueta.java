package dev.charles.gerador_etiquetas.patterns.composite;

import java.util.ArrayList;
import java.util.List;

public class PaginaA4Etiqueta implements ComponenteHtml {

    private List<ComponenteHtml> componentes;

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
                            margin: 10mm;
                        }

                        body {
                            font-family: Arial, sans-serif;
                            margin: 0;
                            padding: 0;
                            background: #eaeaea;
                        }

                        .pagina-a4 {
                            width: 210mm;
                            min-height: 297mm;
                            margin: auto;
                            padding: 10mm;
                            background: white;
                            box-sizing: border-box;

                            display: flex;
                            flex-wrap: wrap;
                            align-content: flex-start;
                            gap: 2mm;
                        }

                        .etiqueta-card {
                            border: 1px solid #000;
                            box-sizing: border-box;
                            padding: 5mm 6mm;

                            display: flex;
                            flex-direction: column;
                            justify-content: space-between;

                            page-break-inside: avoid;
                            overflow: hidden;
                            background: white;
                        }

                        .descricao {
                            font-size: var(--fonte-descricao);
                            font-weight: normal;
                            text-transform: uppercase;
                            line-height: 1.15;
                            overflow: hidden;
                        }

                        .conteudo-etiqueta {
                            display: grid;
                            grid-template-columns: minmax(0, 1fr) max-content;
                            grid-template-rows: auto auto;
                            column-gap: 5mm;
                            row-gap: 2mm;
                            align-items: end;
                        }

                        .prateleira {
                            grid-column: 1;
                            grid-row: 1;
                            font-size: var(--fonte-prateleira);
                            font-weight: 900;
                            line-height: 1.05;
                            letter-spacing: 0.2px;
                            white-space: nowrap;
                            overflow: visible;
                        }

                        .codigo-venda {
                            grid-column: 1;
                            grid-row: 2;
                            font-size: var(--fonte-codigo-venda);
                            font-weight: normal;
                            line-height: 1.1;
                            white-space: nowrap;
                            overflow: hidden;
                            text-overflow: ellipsis;
                        }

                        .codigos-originais {
                            grid-column: 2;
                            grid-row: 1 / span 2;
                            align-self: end;
                            justify-self: end;
                            display: flex;
                            flex-direction: column;
                            align-items: flex-start;
                            max-width: 45mm;
                            overflow: hidden;
                        }

                        .codigo-original {
                            font-size: var(--fonte-codigo-original);
                            font-weight: normal;
                            line-height: 1.15;
                            white-space: nowrap;
                        }

                        @media print {
                            body {
                                background: white;
                            }

                            .pagina-a4 {
                                margin: 0;
                                padding: 10mm;
                            }

                            .etiqueta-card {
                                page-break-inside: avoid;
                                break-inside: avoid;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="pagina-a4">
                """);

        for (ComponenteHtml componente : componentes) {
            html.append(componente.renderizar());
        }

        html.append("""
                    </div>
                </body>
                </html>
                """);

        return html.toString();
    }
}
