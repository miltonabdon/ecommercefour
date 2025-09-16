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

import jakarta.persistence.EntityManager;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class EcommercefourApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommercefourApplication.class, args);
    }

    private static UUID sequentialUUID(long sequence) {
        long now = System.currentTimeMillis();
        long msb = (now << 16) | (sequence & 0xFFFF);
        long lsb = ThreadLocalRandom.current().nextLong();
        return new UUID(msb, lsb);
    }

    @Bean
    CommandLineRunner seedData(ProdutoRepository produtoRepository, PedidoRepository pedidoRepository, UserRepository userRepository, EntityManager entityManager, TransactionTemplate transactionTemplate) {
        return args -> transactionTemplate.execute(status -> {
            // Seed Produtos
            if (produtoRepository.count() == 0) {
                Date now = new Date();
                List<String> categorias = Arrays.asList("Eletrônicos", "Livros", "Casa", "Moda", "Esportes");
                List<Produto> produtos = IntStream.rangeClosed(1, 15)
                        .mapToObj(i -> new Produto(
                                sequentialUUID(i),
                                "Produto " + i,
                                "Descrição do produto número " + i,
                                ThreadLocalRandom.current().nextDouble(10.0, 1000.0),
                                categorias.get(ThreadLocalRandom.current().nextInt(categorias.size())),
                                ThreadLocalRandom.current().nextDouble(1.0, 100.0),
                                now,
                                now
                        ))
                        .collect(Collectors.toList());
                for (Produto p : produtos) {
                    entityManager.persist(p);
                }
            }

            // Seed Pedidos
            if (pedidoRepository.count() == 0) {
                List<Produto> allProdutos = produtoRepository.findAll();
                if (!allProdutos.isEmpty()) {
                    // Work with managed references to avoid detached entity errors when persisting Pedido
                    List<UUID> allIds = new ArrayList<>(allProdutos.stream().map(Produto::getId).toList());
                    for (int i = 1; i <= 5; i++) {
                        int size = ThreadLocalRandom.current().nextInt(1, Math.min(6, allIds.size() + 1));
                        Collections.shuffle(allIds);
                        List<UUID> selectedIds = new ArrayList<>(allIds.subList(0, size));
                        List<Produto> selectedManaged = selectedIds.stream()
                                .map(id -> entityManager.getReference(Produto.class, id))
                                .toList();
                        double total = selectedManaged.stream().map(Produto::getPreco).filter(Objects::nonNull).mapToDouble(Double::doubleValue).sum();
                        Date createdAt = new Date();
                        Pedido novo = new Pedido(sequentialUUID(1000L + i), selectedManaged, Status.PENDENTE, false, total, "admin", createdAt);
                        entityManager.persist(novo);
                    }
                }
            }

            // Seed Users (persist on MySQL)
            if (userRepository.count() == 0) {
                List<User> users = List.of(
                        new User(sequentialUUID(2000L), "Admin", "User", "admin@ecommerce.com"),
                        new User(sequentialUUID(2001L), "Regular", "User", "user@ecommerce.com"),
                        new User(sequentialUUID(2002L), "Maria", "Silva", "maria.silva@example.com"),
                        new User(sequentialUUID(2003L), "João", "Souza", "joao.souza@example.com")
                );
                for (User u : users) {
                    entityManager.persist(u);
                }
            }
            return null;
        });
    }
}
