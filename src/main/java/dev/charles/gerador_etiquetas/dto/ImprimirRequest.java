package dev.charles.gerador_etiquetas.dto;

import java.util.List;

public record ImprimirRequest(List<Long> ids, String modelo) {
}
