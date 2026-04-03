package com.project.api.ecommerce.support;

import com.project.api.ecommerce.dto.response.*;
import com.project.api.ecommerce.model.enums.PedidoStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class DataDtoFactory {

    public static CategoriaProdutoResponseDTO criarCategoriaPadrao() {
        return new CategoriaProdutoResponseDTO(
                1L,
                "Eletrônicos"
        );
    }

    public static CategoriaProdutoResponseDTO criarCategoria(String nome) {
        return new CategoriaProdutoResponseDTO(
                1L,
                nome
        );
    }

    public static ProdutoResponseDTO criarProdutoPadrao() {
        return new ProdutoResponseDTO(
                1L,
                "Notebook",
                "Dell",
                "Notebook Gamer",
                BigDecimal.valueOf(3500.00),
                10,
                criarCategoriaPadrao()
        );
    }

    public static ProdutoResponseDTO criarProdutoComPreco(BigDecimal preco) {
        return new ProdutoResponseDTO(
                1L,
                "Notebook",
                "Dell",
                "Notebook Gamer",
                preco,
                10,
                criarCategoriaPadrao()
        );
    }

    public static ProdutoResponseDTO criarProdutoSemCategoria() {
        return new ProdutoResponseDTO(
                1L,
                "Notebook",
                "Dell",
                "Notebook Gamer",
                BigDecimal.valueOf(3500.00),
                10,
                criarCategoriaPadrao()
        );
    }

    public static CarrinhoItemResponseDTO criarItemPadrao() {

        ProdutoResponseDTO produto = criarProdutoPadrao();
        int quantidade = 2;

        return new CarrinhoItemResponseDTO(
                1L,
                produto,
                quantidade,
                calcularTotal(produto, quantidade)
        );
    }

    public static CarrinhoItemResponseDTO criarItemComQuantidade(int quantidade) {

        ProdutoResponseDTO produto = criarProdutoPadrao();

        return new CarrinhoItemResponseDTO(
                1L,
                produto,
                quantidade,
                calcularTotal(produto, quantidade)
        );
    }

    private static BigDecimal calcularTotal(ProdutoResponseDTO produto, int quantidade) {
        return produto.precoUnitario()
                .multiply(BigDecimal.valueOf(quantidade));
    }

    public static CarrinhoResponseDTO criarCarrinhoResponsePadrao() {

        CarrinhoItemResponseDTO item = criarItemPadrao();
        Set<CarrinhoItemResponseDTO> itens = Set.of(item);
        BigDecimal valorTotal = calcularValorTotal(itens);

        return new CarrinhoResponseDTO(1L, itens, valorTotal );
    }

    private static BigDecimal calcularValorTotal(Set<CarrinhoItemResponseDTO> itens) {
        return itens.stream()
                .map(CarrinhoItemResponseDTO::precoTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static ProdutoCategoriaResponseDTO criarProdutoCategoriaPadrao() {
        return new ProdutoCategoriaResponseDTO(
                1L,
                "Notebook",
                "Dell",
                "Notebook Gamer",
                BigDecimal.valueOf(3500.00),
                10
        );
    }

    public static ProdutoCategoriaResponseDTO criarProdutoCategoriaComPreco(BigDecimal preco) {
        return new ProdutoCategoriaResponseDTO(
                1L,
                "Notebook",
                "Dell",
                "Notebook Gamer",
                preco,
                10
        );
    }

    public static CategoriaResponseDTO categoriaResponsePadrao() {
        return new CategoriaResponseDTO(
                1L,
                "Eletrônicos",
                List.of( criarProdutoCategoriaPadrao() )
        );
    }

    public static ImagemResponseDTO imagemResponsePadrao(){
        return new ImagemResponseDTO(
                1L,
                "Descrição da imagem",
                "http://localhost:8080/api/v1/imagens/1"
        );
    }

    public static PedidoResponseDTO criarPedidoPadrao() {
        return new PedidoResponseDTO(
                1L,
                criarUsuarioPadrao(),
                Set.of( criarPedidoItem() ),
                PedidoStatus.ENTREGUE,
                new BigDecimal("3800.00"),
                LocalDate.now()
        );
    }

    public static UsuarioResponseDTO criarUsuarioPadrao(){
        return new UsuarioResponseDTO(
                1L,
                "User 1",
                "user1@email.com"
        );
    }

    public static PedidoItemResponseDTO criarPedidoItem(){
        return new PedidoItemResponseDTO(
                1L,
                1,
                new BigDecimal("3500.00"),
                new BigDecimal("3500.00"),
                criarProdutoPadrao()
        );
    }
}