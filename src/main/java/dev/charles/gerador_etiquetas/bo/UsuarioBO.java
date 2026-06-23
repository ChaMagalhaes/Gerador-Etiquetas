package dev.charles.gerador_etiquetas.bo;

import dev.charles.gerador_etiquetas.dao.UsuarioDAO;
import dev.charles.gerador_etiquetas.model.Usuario;

import java.util.List;

public class UsuarioBO {

    private UsuarioDAO usuarioDAO;

    public UsuarioBO() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario autenticarUsuario(String email, String senha) {
        validarLogin(email, senha);

        Usuario usuario = usuarioDAO.autenticar(email.trim(), senha);

        if (usuario == null) {
            throw new RuntimeException("E-mail ou senha inválidos.");
        }

        return usuario;
    }

    public void cadastrarUsuario(String nome, String login, String senha, String email, String telefone) {
        validarCadastro(nome, login, senha, email);

        login = login.trim();

        if (usuarioDAO.buscarPorLogin(login) != null) {
            throw new RuntimeException("Já existe um usuário com esse login.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome.trim());
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setEmail(email.trim());
        usuario.setTelefone(telefone);

        usuarioDAO.salvar(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listar();
    }

    private void validarLogin(String email, String senha) {
        if (email == null || email.isBlank()) {
            throw new RuntimeException("O e-mail é obrigatório.");
        }

        if (senha == null || senha.isBlank()) {
            throw new RuntimeException("A senha é obrigatória.");
        }
    }

    private void validarCadastro(String nome, String login, String senha, String email) {
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
    }
}