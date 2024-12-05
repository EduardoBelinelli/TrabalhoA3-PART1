package com.loja.loja_produtos.controllers;

import com.loja.loja_produtos.dtos.CompraDTO;
import com.loja.loja_produtos.models.*;
import com.loja.loja_produtos.repositories.ClienteRepository;
import com.loja.loja_produtos.repositories.CompraRepository;
import com.loja.loja_produtos.repositories.ProdutoRepository;
import com.loja.loja_produtos.repositories.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/compra")
public class CompraController {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    // Receber pedido de compra
    @PostMapping
    public ResponseEntity<CompraDTO> criarPedidoDeCompra(@RequestBody CompraDTO compraDTO) {
        // Verificar se o cliente existe
        Cliente cliente = clienteRepository.findById(compraDTO.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        // Verificar se o vendedor existe
        Vendedor vendedor = vendedorRepository.findById(compraDTO.getVendedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));

        // Contar as ocorrências dos IDs de produtos
        Map<Long, Long> produtoQuantidadeMap = compraDTO.getProdutosIds().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Verificar se os produtos existem
        List<Produto> produtos = produtoRepository.findAllById(produtoQuantidadeMap.keySet());
        if (produtos.size() != produtoQuantidadeMap.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more products were not found");
        }

        // Verificar o estoque e reduzir conforme as quantidades
        produtos.forEach(produto -> {
            Long quantidadeRequerida = produtoQuantidadeMap.get(produto.getId());
            if (produto.getEstoque() < quantidadeRequerida) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Not enough stock for product: " + produto.getName());
            }
            produto.setEstoque(produto.getEstoque() - quantidadeRequerida.intValue());
        });

        // Atualizar o estoque dos produtos
        produtoRepository.saveAllAndFlush(produtos);

        // Criar a entidade Compra
        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setVendedor(vendedor);
        compra.setDataCompra(LocalDateTime.now());

        // Criar as associações CompraProduto
        List<CompraProduto> compraProdutos = produtoQuantidadeMap.entrySet().stream()
                .map(entry -> {
                    Produto produto = produtos.stream()
                            .filter(p -> p.getId().equals(entry.getKey()))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
                    CompraProduto compraProduto = new CompraProduto();
                    compraProduto.setCompra(compra);
                    compraProduto.setProduto(produto);
                    compraProduto.setQuantidade(entry.getValue().intValue());
                    return compraProduto;
                })
                .toList();

        compra.setCompraProdutos(compraProdutos);

        // Salvar a compra no banco de dados
        Compra novaCompra = compraRepository.saveAndFlush(compra);

        // Converter para DTO antes de retornar
        CompraDTO novaCompraDTO = new CompraDTO(
                novaCompra.getId(),
                novaCompra.getCliente().getId(),
                novaCompra.getVendedor().getId(),
                compraProdutos.stream()
                        .flatMap(cp -> Collections.nCopies(cp.getQuantidade(), cp.getProduto().getId()).stream())
                        .toList()
        );

        return ResponseEntity.ok(novaCompraDTO);
    }


    // Cancelar pedido de compra
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedidoDeCompra(@PathVariable Long id) {
        // Verificar se a compra existe
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found"));

        // Restaurar o estoque dos produtos com base nas quantidades em CompraProduto
        List<CompraProduto> compraProdutos = compra.getCompraProdutos();
        compraProdutos.forEach(cp -> {
            Produto produto = cp.getProduto();
            produto.setEstoque(produto.getEstoque() + cp.getQuantidade()); // Restaura o estoque com base na quantidade
        });

        // Atualizar os estoques no banco de dados
        produtoRepository.saveAllAndFlush(
                compraProdutos.stream().map(CompraProduto::getProduto).toList()
        );

        // Deletar a compra
        compraRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}

