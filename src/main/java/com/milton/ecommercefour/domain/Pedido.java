package com.milton.ecommercefour.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    private UUID id;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "pedido_produto",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id"))
    private List<Produto> produtos;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Boolean pago;

    @Column(name = "valorTotal")
    private Double valorTotal;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "dataCriacao")
    private Date dataCriacao;

    public Pedido() {
    }

    public Pedido(UUID id, List<Produto> produtos, Status status, Boolean pago, Double valorTotal, String createdBy, Date dataCriacao) {
        this.id = id;
        this.produtos = produtos;
        this.status = status;
        this.pago = pago;
        this.valorTotal = valorTotal;
        this.createdBy = createdBy;
        this.dataCriacao = dataCriacao;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
