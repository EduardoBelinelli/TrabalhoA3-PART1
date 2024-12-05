package com.loja.loja_produtos.repositories;

import com.loja.loja_produtos.models.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraRepository extends JpaRepository<Compra, Long> {
}
