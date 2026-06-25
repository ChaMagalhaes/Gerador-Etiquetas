package dev.charles.gerador_etiquetas.util;

import dev.charles.gerador_etiquetas.model.Usuario;

public class SessaoUsuario {
    private static Usuario usuarioLogado;

    private SessaoUsuario() {
    }

    public static void iniciar(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static Long getUsuarioIdLogado() {
        if (usuarioLogado == null) {
            return null;
        }

        return usuarioLogado.getId();
    }

    public static void encerrar() {
        usuarioLogado = null;
    }
}
