package com.loja.loja_produtos.controllers;

import com.loja.loja_produtos.models.Vendedor;
import com.loja.loja_produtos.repositories.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendedor")
public class VendedorController {

    private final VendedorRepository vendedorRepository;

    @Autowired
    public VendedorController(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    // Criar um vendedor
    @PostMapping
    public Vendedor createVendedor(@RequestBody Vendedor vendedor) {
        return vendedorRepository.saveAndFlush(vendedor);
    }

    // Listar todos os vendedores
    @GetMapping
    public List<Vendedor> getVendedores() {
        return vendedorRepository.findAll();
    }

    // Obter vendedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<Vendedor> getVendedorById(@PathVariable Long id) {
        return vendedorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Atualizar vendedor
    @PutMapping("/{id}")
    public ResponseEntity<Vendedor> updateVendedor(@PathVariable Long id, @RequestBody Vendedor vendedorAtualizado) {
        return vendedorRepository.findById(id)
                .map(vendedor -> {
                    vendedor.setName(vendedorAtualizado.getName());
                    return ResponseEntity.ok(vendedorRepository.saveAndFlush(vendedor));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Deletar vendedor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendedor(@PathVariable Long id) {
        if (vendedorRepository.existsById(id)) {
            vendedorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

