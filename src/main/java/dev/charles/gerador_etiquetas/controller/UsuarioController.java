package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.dao.UsuarioDAO;
import dev.charles.gerador_etiquetas.model.Usuario;

import java.util.List;

public class UsuarioController {

    private UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario autenticarUsuario(String email, String senha) {
        if (email == null || email.isBlank()) {
            throw new RuntimeException("O e-mail é obrigatório.");
        }

        if (senha == null || senha.isBlank()) {
            throw new RuntimeException("A senha é obrigatória.");
        }

        Usuario usuario = usuarioDAO.autenticar(email.trim(), senha);

        if (usuario == null) {
            throw new RuntimeException("E-mail ou senha inválidos.");
        }

        return usuario;
    }

    public void cadastrarUsuario(String nome, String login, String senha, String email, String telefone) {
        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("O nome do usuário é obrigatório.");
        }

        if (login == null || login.isBlank()) {
            throw new RuntimeException("O login do usuário é obrigatório.");
        }

        if (senha == null || senha.isBlank()) {
            throw new RuntimeException("A senha do usuário é obrigatória.");
        }

        if (email == null || email.isBlank()) {
            throw new RuntimeException("O e-mail do usuário é obrigatório.");
        }

        if (usuarioDAO.buscarPorLogin(login) != null) {
            throw new RuntimeException("Já existe um usuário com esse login.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);

        usuarioDAO.salvar(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listar();
    }
}
