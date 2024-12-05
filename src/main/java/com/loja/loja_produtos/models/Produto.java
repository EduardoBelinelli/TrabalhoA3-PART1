package com.loja.loja_produtos.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a product in the application.
 * It is annotated with JPA annotations to be mapped to a database table.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Produto {

    /**
     * The unique identifier of the product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The type of the product.
     */
    private String name;

    /**
     * The amount available of the product.
     */
    private int estoque;

    /**
     * The price of the product.
     */
    private double price;
    @JsonIgnore
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<CompraProduto> compraProdutos = new ArrayList<>();

}
