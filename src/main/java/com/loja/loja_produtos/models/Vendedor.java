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
 * Represents an employee in the system.
 * This class is an entity that stores information about a Vendedor (employee).
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vendedor {
    /**
     * The unique identifier for the employee.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The employee's name.
     */
    private String name;
    /**
     * The employee's salary.
     */
    private double salary;

    @JsonIgnore
    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL)
    private List<Compra> vendas = new ArrayList<>();
}
