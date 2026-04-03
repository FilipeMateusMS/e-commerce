package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.filters.ProdutoFilterDTO;
import com.project.api.ecommerce.dto.request.ProdutoRequestDTO;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.dto.response.ProdutoResponseDTO;
import com.project.api.ecommerce.mappers.ProdutoMapper;
import com.project.api.ecommerce.model.Categoria;
import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.repository.CategoriaRepository;
import com.project.api.ecommerce.repository.ProdutoRepository;
import com.project.api.ecommerce.support.DataDtoFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoService service;

    @Test
    @DisplayName("Deve buscar um produto pelo ID e retornar seu DTO de resposta com sucesso")
    void deveBuscarProdutoPorIdComSucesso() {
        Produto produto = new Produto();
        ProdutoResponseDTO dto = DataDtoFactory.criarProdutoPadrao();

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoMapper.toDTO(produto)).thenReturn(dto);

        ProdutoResponseDTO response = service.getProdutoById(1L);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    @DisplayName("Deve listar produtos utilizando filtros dinâmicos e paginação com sucesso")
    void deveListarProdutosFiltradosComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Produto produto = new Produto();
        ProdutoResponseDTO dto = DataDtoFactory.criarProdutoPadrao();
        Page<Produto> page = new PageImpl<>(List.of(produto));

        when(produtoRepository.findAll((Specification<Produto>) any(), eq(pageable))).thenReturn(page);
        when(produtoMapper.toDTO(produto)).thenReturn(dto);

        PageResponse<ProdutoResponseDTO> response = service.getAllProdutosFiltered(new ProdutoFilterDTO(), pageable);

        assertThat(response.content() ).hasSize(1);
    }

    @Test
    @DisplayName("Deve cadastrar um novo produto validando a categoria e a unicidade do nome")
    void deveInserirProdutoComSucesso() {
        ProdutoRequestDTO request = new ProdutoRequestDTO(
                "Produto",
                "Samsung",
                "Produto de alta qualidade",
                new BigDecimal("1500.00"),
                1,
                "Eletrônicos"
        );

        Categoria categoria = new Categoria("Eletrônicos");

        Produto produto = new Produto();
        produto.setNome("Produto");

        Produto produtoSalvo = new Produto();

        ProdutoResponseDTO dto = DataDtoFactory.criarProdutoPadrao();

        when(categoriaRepository.findByNome("Eletrônicos")).thenReturn(categoria);
        when(produtoMapper.toProduto(request, categoria)).thenReturn(produto);
        when(produtoRepository.existsByNomeIgnoreCase("Produto")).thenReturn(false);
        when(produtoRepository.save(produto)).thenReturn(produtoSalvo);
        when(produtoMapper.toDTO(produtoSalvo)).thenReturn(dto);

        ProdutoResponseDTO response = service.insertProduto(request);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    @DisplayName("Deve remover um produto do sistema após confirmar sua existência pelo ID")
    void deveDeletarProdutoComSucesso() {
        when(produtoRepository.existsById(1L)).thenReturn(true);

        service.deleteProdutoById(1L);

        verify(produtoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve atualizar os dados de um produto existente validando se o novo nome já está em uso por outro ID")
    void deveAtualizarProdutoComSucesso() {
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);

        ProdutoRequestDTO request = new ProdutoRequestDTO(
                "Produto",
                "Samsung",
                "Produto de alta qualidade",
                new BigDecimal("1500.00"),
                1,
                "Eletrônicos"
        );

        Categoria categoria = new Categoria("Eletrônicos");

        Produto produtoSalvo = new Produto();
        ProdutoResponseDTO dto = DataDtoFactory.criarProdutoPadrao();

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.existsByNomeIgnoreCaseAndIdNot(anyString(), anyLong() )).thenReturn(false);
        when(categoriaRepository.findByNome("Eletrônicos")).thenReturn(categoria);
        when(produtoRepository.save(produtoExistente)).thenReturn(produtoSalvo);
        when(produtoMapper.toDTO(produtoSalvo)).thenReturn(dto);

        ProdutoResponseDTO response = service.updateProduto(1L, request);

        assertThat(response).isEqualTo(dto);
        assertThat(produtoExistente.getNome()).isEqualTo("Produto");
    }
}