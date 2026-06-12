package dev.charles.gerador_etiquetas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Etiqueta {
    @Id
    @GeneratedValue
    private Long id;
    private String descricao;
    private String codigoVenda;
    private String codigoOriginal;
    private String localPrateleira;
}
