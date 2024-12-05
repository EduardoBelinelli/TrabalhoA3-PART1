package com.loja.loja_produtos.repositories;

import com.loja.loja_produtos.models.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendedorRepository extends JpaRepository<Vendedor,Long> {
}
