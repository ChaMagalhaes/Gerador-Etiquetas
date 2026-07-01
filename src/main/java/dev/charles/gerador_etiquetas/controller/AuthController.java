package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.UsuarioBO;
import dev.charles.gerador_etiquetas.model.Usuario;
import dev.charles.gerador_etiquetas.util.SessaoUsuario;
import dev.charles.gerador_etiquetas.dto.LoginRequest;
import dev.charles.gerador_etiquetas.dto.UsuarioResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioBO usuarioBO = new UsuarioBO();

    @PostMapping("/login")
    public UsuarioResponse login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioBO.autenticarUsuario(request.email(), request.senha());
        SessaoUsuario.iniciar(usuario);
        return UsuarioResponse.de(usuario);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        SessaoUsuario.encerrar();
    }
}
