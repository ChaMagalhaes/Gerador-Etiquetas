package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.bo.PrateleiraBO;
import dev.charles.gerador_etiquetas.model.Prateleira;
import dev.charles.gerador_etiquetas.dto.PrateleiraRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prateleiras")
public class PrateleiraRestController {

    private final PrateleiraBO prateleiraBO = new PrateleiraBO();

    @GetMapping
    public List<Prateleira> listar() {
        return prateleiraBO.listarPrateleiras();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prateleira criar(@RequestBody PrateleiraRequest request) {
        return prateleiraBO.cadastrarPrateleira(request.localPrateleira(), request.descricaoGrupo());
    }
}
