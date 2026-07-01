package dev.charles.gerador_etiquetas.dto;

import dev.charles.gerador_etiquetas.model.Usuario;

public record UsuarioResponse(Long id, String nome, String login, String email, String telefone) {

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }
}
