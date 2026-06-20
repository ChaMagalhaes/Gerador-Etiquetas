package dev.charles.gerador_etiquetas;
import dev.charles.gerador_etiquetas.view.TelaLogin;

import javax.swing.*;

public class GeradorEtiquetasApplication {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			TelaLogin telaLogin = new TelaLogin();
			telaLogin.setVisible(true);
		});
	}
}
