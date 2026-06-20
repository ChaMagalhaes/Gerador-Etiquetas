package dev.charles.gerador_etiquetas.view;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeradorArquivoHtml {

    public void salvar(String conteudoHtml, String nomeArquivo) {
        try {
            File arquivo = new File(nomeArquivo);

            try (FileWriter writer = new FileWriter(arquivo)) {
                writer.write(conteudoHtml);
            }

            System.out.println("Arquivo HTML gerado com sucesso: " + arquivo.getAbsolutePath());
            abrirNoNavegador(arquivo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar arquivo HTML.", e);
        }
    }

    private void abrirNoNavegador(File arquivo) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(arquivo.toURI());
            } else {
                System.out.println("Abertura automática no navegador não é suportada neste sistema.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao abrir HTML no navegador.", e);
        }
    }
}
