package com.milton.ecommercefour;

import com.milton.ecommercefour.domain.Pedido;
import com.milton.ecommercefour.domain.Produto;
import com.milton.ecommercefour.domain.User;
import com.milton.ecommercefour.domain.Status;
import com.milton.ecommercefour.repository.PedidoRepository;
import com.milton.ecommercefour.repository.ProdutoRepository;
import com.milton.ecommercefour.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class EcommercefourApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommercefourApplication.class, args);
    }

    @Bean
    CommandLineRunner seedData(ProdutoRepository produtoRepository, PedidoRepository pedidoRepository, UserRepository userRepository) {
        return args -> {
            // Seed Produtos
            if (produtoRepository.count() == 0) {
                Date now = new Date();
                List<String> categorias = Arrays.asList("Eletrônicos", "Livros", "Casa", "Moda", "Esportes");
                List<Produto> produtos = IntStream.rangeClosed(1, 15)
                        .mapToObj(i -> new Produto(
                                null,
                                "Produto " + i,
                                "Descrição do produto número " + i,
                                ThreadLocalRandom.current().nextDouble(10.0, 1000.0),
                                categorias.get(ThreadLocalRandom.current().nextInt(categorias.size())),
                                ThreadLocalRandom.current().nextDouble(1.0, 100.0),
                                now,
                                now
                        ))
                        .collect(Collectors.toList());
                produtoRepository.saveAll(produtos);
            }

            // Seed Pedidos
            if (pedidoRepository.count() == 0) {
                List<Produto> allProdutos = produtoRepository.findAll();
                if (!allProdutos.isEmpty()) {
                    List<Pedido> pedidos = new ArrayList<>();
                    for (int i = 1; i <= 5; i++) {
                        int size = ThreadLocalRandom.current().nextInt(1, Math.min(6, allProdutos.size() + 1));
                        Collections.shuffle(allProdutos);
                        List<Produto> selected = new ArrayList<>(allProdutos.subList(0, size));
                        double total = selected.stream().map(Produto::preco).filter(Objects::nonNull).mapToDouble(Double::doubleValue).sum();
                        Date createdAt = new Date();
                        pedidos.add(new Pedido(null, selected, Status.PENDENTE, false, total, null, createdAt));
                    }
                    pedidoRepository.saveAll(pedidos);
                }
            }

            // Seed Users (persist on MySQL)
            if (userRepository.count() == 0) {
                List<User> users = List.of(
                        new User(null, "Admin", "User", "admin@ecommerce.com"),
                        new User(null, "Regular", "User", "user@ecommerce.com"),
                        new User(null, "Maria", "Silva", "maria.silva@example.com"),
                        new User(null, "João", "Souza", "joao.souza@example.com")
                );
                userRepository.saveAll(users);
            }
        };
    }
}
