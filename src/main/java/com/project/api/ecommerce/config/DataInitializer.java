package com.project.api.ecommerce.config;

import com.project.api.ecommerce.enums.PedidoStatus;
import com.project.api.ecommerce.model.*;
import com.project.api.ecommerce.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Fazer operações quando a aplicação subir o contexto
@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, RoleRepository roleRepository, CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository, PedidoRepository pedidoRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (usuarioRepository.count() > 0 ) {
            return; // evita duplicação
        }
        // ROLES
        Role adminRole = roleRepository.save( new Role("ADMIN" ) );
        Role userRole = roleRepository.save( new Role("USER" ) );

        // USUÁRIOS
        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setEmail("admin@email.com");
        admin.setSenha(passwordEncoder.encode("123456"));
        admin.setTelefone("11999999999");
        admin.setRoles(List.of(adminRole));

        Usuario user = new Usuario();
        user.setNome("João Silva");
        user.setEmail("user@email.com");
        user.setSenha(passwordEncoder.encode("123456"));
        user.setTelefone("11988888888");
        user.setRoles(List.of(userRole));

        usuarioRepository.saveAll( List.of( admin, user ) );

        // CATEGORIAS
        Categoria eletronicos = categoriaRepository.save( new Categoria("Eletrônicos") );
        Categoria livros = categoriaRepository.save( new Categoria( "Livros" ) );

        // PRODUTOS
        Produto notebook = new Produto();
        notebook.setNome("Notebook Dell");
        notebook.setMarca("Dell");
        notebook.setDescricao("Notebook 16GB RAM");
        notebook.setPrecoUnitario(new BigDecimal("4500.00"));
        notebook.setQuantidade(10);
        notebook.setCategoria(eletronicos);

        Produto livroJava = new Produto();
        livroJava.setNome("Java Completo");
        livroJava.setMarca("Casa do Código");
        livroJava.setDescricao("Livro avançado de Java");
        livroJava.setPrecoUnitario(new BigDecimal("120.00"));
        livroJava.setQuantidade(50);
        livroJava.setCategoria(livros);

        produtoRepository.saveAll(List.of(notebook, livroJava));

        // CARRINHO
        Carrinho carrinho = new Carrinho();
        carrinho.setUsuario(user);

        CarrinhoItem item = new CarrinhoItem();
        item.setProduto(notebook);
        item.setQuantidade(1);
        item.setCarrinho(carrinho);

        carrinho.getItensCarrinho().add(item);

        user.setCarrinho(carrinho);
        usuarioRepository.save(user);

        // PEDIDO
        Pedido pedido = new Pedido();
        pedido.setUsuario(user);
        pedido.setDataPedido(LocalDate.now());
        pedido.setPedidoStatus(PedidoStatus.PROCESSANDO);
        pedido.setPrecoVendaTotal(new BigDecimal("4500.00"));

        PedidoItem pedidoItem = new PedidoItem();
        pedidoItem.setPedido(pedido);
        pedidoItem.setProduto(notebook);
        pedidoItem.setQuantidade(1);
        pedidoItem.setPrecoVendaUnitario(new BigDecimal("4500.00"));

        pedido.getItensPedidos().add(pedidoItem);

        pedidoRepository.save(pedido);
    }
}
