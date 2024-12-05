package com.loja.loja_produtos.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a purchase in the e-commerce application.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Compra {
    /**
     * The unique identifier for the purchase.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The client who made the purchase.
     * It is annotated with {@link ManyToOne} to indicate a many-to-one relationship with the {@link Cliente} entity.
     * The foreign key column is named "cliente_id" and it is not nullable.
     */
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /**
     * The seller who processed the purchase.
     * It is annotated with {@link ManyToOne} to indicate a many-to-one relationship with the {@link Vendedor} entity.
     * The foreign key column is named "vendedor_id" and it is not nullable.
     */
    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Vendedor vendedor;
    
    /**
     * The date and time when the purchase was made.
     */
    private LocalDateTime dataCompra;

    /**
     * The list of products included in the purchase.
     * It is annotated with {@link ManyToMany} to indicate a many-to-many relationship with the {@link Produto} entity.
     * The join table is named "compra_produto", and the join columns are "compra_id" and "produto_id".
     */
    @JsonIgnore
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompraProduto> compraProdutos = new ArrayList<>();

    @Column(name = "VALORTOTAL")
    private double valorTotal;

    public double sumTotalValue() {
        return compraProdutos.stream()
                .mapToDouble(cp -> cp.getQuantidade() * cp.getProduto().getPrice())
                .sum();
    }

    @PrePersist
    @PreUpdate
    public void updateTotalValue() {
        this.valorTotal = sumTotalValue();
    }

}