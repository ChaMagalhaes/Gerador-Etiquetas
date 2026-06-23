package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.UsuarioBO;
import dev.charles.gerador_etiquetas.model.Usuario;

import java.util.List;

public class UsuarioController {

    private UsuarioBO usuarioBO;

    public UsuarioController() {
        this.usuarioBO = new UsuarioBO();
    }

    public Usuario autenticarUsuario(String email, String senha) {
        return usuarioBO.autenticarUsuario(email, senha);
    }

    public void cadastrarUsuario(String nome, String login, String senha, String email, String telefone) {
        usuarioBO.cadastrarUsuario(nome, login, senha, email, telefone);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioBO.listarUsuarios();
    }
}