package com.loja.loja_produtos.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a client entity in the system.
 * This class is mapped to a database table using JPA annotations.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cliente {
    /**
     * The unique identifier for the client.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the client.
     */
    @Column(name = "NAME")
    private String name;

    /**
     * The CPF (Brazilian individual taxpayer registry identification) of the client.
     */
    @Column(name = "CPF")
    private Long cpf;
}
