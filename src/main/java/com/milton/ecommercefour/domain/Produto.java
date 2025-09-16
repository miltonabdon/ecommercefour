package com.milton.ecommercefour.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    private @Id UUID id;
    private String nome;
    private String descricao;
    private Double preco;
    private String categoria;
    private Double quantidadeEstoque;
    private Date dataCriacao;
    private Date dataAtualizacao;

}
