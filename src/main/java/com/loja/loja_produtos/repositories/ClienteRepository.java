package com.loja.loja_produtos.repositories;

import com.loja.loja_produtos.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente,Long> {
}
