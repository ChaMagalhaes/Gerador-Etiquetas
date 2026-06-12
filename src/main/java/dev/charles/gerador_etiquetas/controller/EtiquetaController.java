package dev.charles.gerador_etiquetas.controller;

import dev.charles.gerador_etiquetas.model.Etiqueta;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EtiquetaController {
    @PostMapping
    public Etiqueta post(@RequestBody Etiqueta etiqueta) {
        return etiqueta;
    }
}
