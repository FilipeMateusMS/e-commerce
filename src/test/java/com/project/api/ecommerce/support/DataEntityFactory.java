package com.project.api.ecommerce.support;

import com.project.api.ecommerce.model.*;
import com.project.api.ecommerce.model.enums.PedidoStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class DataEntityFactory {

    public static Usuario usuario() {
        String uuid = UUID.randomUUID().toString();

        Usuario usuario = new Usuario();
        usuario.setNome("user" + uuid);
        usuario.setEmail("user" + uuid + "@email.com");
        usuario.setSenha("123456");
        usuario.setTelefone("61999999999");

        return usuario;
    }

    public static CarrinhoItem carrinhoItem(Carrinho carrinho, Produto produto) {
        CarrinhoItem item = new CarrinhoItem();
        item.setProduto( produto );
        item.setCarrinho( carrinho );
        item.setQuantidade(1);
        return item;
    }

    public static Categoria categoria() {
        return new Categoria( "Categoria " + UUID.randomUUID() ) ;
    }

    public static Categoria categoria(String nome) {
        return new Categoria( nome );
    }

    public static Produto produto(Categoria categoria) {
        Produto produto = new Produto();
        produto.setNome("Produto " + UUID.randomUUID() );
        produto.setDescricao("Descrição do produto");
        produto.setCategoria(categoria);
        produto.setPrecoUnitario(BigDecimal.valueOf(10));
        produto.setMarca("Marca de teste");
        produto.setQuantidade(10);
        return produto;
    }

    public static Carrinho carrinho(Usuario usuario) {
        Carrinho carrinho = new Carrinho();
        carrinho.setUsuario(usuario);
        return carrinho;
    }

    public static Imagem imagem(Produto produto) {
        String uuid = UUID.randomUUID().toString();

        Imagem imagem = new Imagem();
        imagem.setNome( "Nome da imagem" + uuid );
        imagem.setProduto( produto );
        imagem.setStorageKey( "storage-key" + uuid );
        imagem.setFileType( ".jpg" );
        imagem.setDescricao( "Descrição da imagem");
        return imagem;
    }

    public static Pedido pedido( Usuario usuario, Set<PedidoItem> itensPedidos ) {

        Pedido pedido = new Pedido();
        pedido.setPedidoStatus( PedidoStatus.PROCESSANDO );
        pedido.setPrecoVendaTotal( BigDecimal.valueOf( 100.00 ) );
        pedido.setDataPedido( LocalDate.now() );
        pedido.setUsuario( usuario );
        pedido.setItensPedidos( itensPedidos );

        return pedido;
    }

    public static Pedido pedido( Usuario usuario ) {

        Pedido pedido = new Pedido();
        pedido.setPedidoStatus( PedidoStatus.PROCESSANDO );
        pedido.setPrecoVendaTotal( BigDecimal.valueOf( 100.00 ) );
        pedido.setDataPedido( LocalDate.now() );
        pedido.setUsuario( usuario );

        return pedido;
    }


    public static PedidoItem pedidoItem( Pedido pedido, Produto produto ) {

        PedidoItem pedidoItem = new PedidoItem();
        pedidoItem.setProduto( produto );
        pedidoItem.setQuantidade( 1 );
        pedidoItem.setPedido( pedido );
        pedidoItem.setPrecoVendaUnitario( BigDecimal.valueOf( 10.00 ));
        return pedidoItem;
    }

    public static Role roleAdmin() {
        Role role = new Role();
        role.setNome( "ADMIN" );
        return role;
    }
}
