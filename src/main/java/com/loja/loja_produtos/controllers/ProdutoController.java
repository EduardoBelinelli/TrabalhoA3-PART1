package com.loja.loja_produtos.controllers;

import com.loja.loja_produtos.models.Produto;
import com.loja.loja_produtos.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // Criar um produto
    @PostMapping
    public Produto createProduto(@RequestBody Produto produto) {
        return produtoRepository.saveAndFlush(produto);
    }

    // Listar todos os produtos
    @GetMapping
    public List<Produto> getProdutos() {
        return produtoRepository.findAll();
    }

    // Obter produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Atualizar produto
    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setName(produtoAtualizado.getName());
                    produto.setPrice(produtoAtualizado.getPrice());
                    return ResponseEntity.ok(produtoRepository.saveAndFlush(produto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Deletar produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

