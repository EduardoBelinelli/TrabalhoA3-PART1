package com.loja.loja_produtos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This class represents a DTO (Data Transfer Object) for a purchase in the application.
 * It contains the necessary information to create or update a purchase record.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompraDTO {

    /**
     * The unique identifier of the purchase.
     */
    private Long id;

    /**
     * The identifier of the client associated with the purchase.
     */
    private Long clienteId;

    /**
     * The identifier of the seller responsible for the purchase.
     */
    private Long vendedorId;

    /**
     * A list of identifiers of the products purchased.
     */
    private List<Long> produtosIds;

}
