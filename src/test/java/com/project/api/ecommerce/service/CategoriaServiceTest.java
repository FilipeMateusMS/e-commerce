package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.request.CategoriaCreateRequestDTO;
import com.project.api.ecommerce.dto.request.CategoriaUpdateRequestDTO;
import com.project.api.ecommerce.dto.response.CategoriaResponseDTO;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.mappers.CategoriaMapper;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private CategoriaService service;

    @Test
    @DisplayName("Deve buscar uma categoria pelo ID e retornar seu DTO de resposta com sucesso")
    void deveBuscarCategoriaPorIdComSucesso() {
        Categoria categoria = new Categoria();
        CategoriaResponseDTO dto = DataDtoFactory.categoriaResponsePadrao();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaMapper.toDTO(categoria)).thenReturn(dto);

        CategoriaResponseDTO response = service.findCategoriaById(1L);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    @DisplayName("Deve listar todas as categorias de forma paginada quando nenhum filtro de nome é fornecido")
    void deveListarCategoriasSemFiltroComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Categoria categoria = new Categoria();
        CategoriaResponseDTO dto = DataDtoFactory.categoriaResponsePadrao();
        Page<Categoria> page = new PageImpl<>(List.of(categoria));

        when(categoriaRepository.findAll(pageable)).thenReturn(page);
        when(categoriaMapper.toDTO(categoria)).thenReturn(dto);

        PageResponse<CategoriaResponseDTO> response = service.findAllByContendoNome(null, pageable);

        assertThat(response.content()).hasSize(1);
    }

    @Test
    @DisplayName("Deve criar uma nova categoria vinculando os produtos informados e validar duplicidade de nome")
    void deveAdicionarCategoriaComSucesso() {
        CategoriaCreateRequestDTO request = new CategoriaCreateRequestDTO("Eletrônicos", List.of(1L, 2L));

        Produto p1 = new Produto(); p1.setId(1L);
        Produto p2 = new Produto(); p2.setId(2L);

        Categoria categoriaSalva = new Categoria();
        CategoriaResponseDTO dto = DataDtoFactory.categoriaResponsePadrao();

        when( categoriaRepository.existsByNome("Eletrônicos")).thenReturn(false);
        when( produtoRepository.findByIdIn(any())).thenReturn(List.of(p1, p2));
        when( categoriaRepository.save(any())).thenReturn(categoriaSalva);
        when( categoriaMapper.toDTO(categoriaSalva)).thenReturn(dto);

        CategoriaResponseDTO response = service.addCategoria(request);

        assertThat(response).isEqualTo(dto);
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deve vincular um produto a uma categoria específica e persistir a alteração no banco")
    void deveAdicionarProdutoNaCategoriaComSucesso() {
        Categoria categoria = new Categoria();
        Produto produto = new Produto();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(produto));

        service.adicionarProdutoNaCategoria(1L, 2L);

        verify(produtoRepository).saveAndFlush(produto);
        assertThat(produto.getCategoria()).isEqualTo(categoria);
    }

    @Test
    @DisplayName("Deve atualizar os dados de uma categoria existente e atualizar sua lista de produtos")
    void deveAtualizarCategoriaComSucesso() {
        Categoria categoria = new Categoria();
        categoria.setNome("Antigo");

        CategoriaUpdateRequestDTO request = new CategoriaUpdateRequestDTO("Novo", List.of(1L));
        Produto produto = new Produto(); produto.setId(1L);

        Categoria categoriaSalva = new Categoria();
        CategoriaResponseDTO dto = DataDtoFactory.categoriaResponsePadrao();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.existsByNomeAndIdNot("Novo", 1L)).thenReturn(false);
        when(produtoRepository.findByIdIn(any())).thenReturn(List.of(produto));
        when(categoriaRepository.save(categoria)).thenReturn(categoriaSalva);
        when(categoriaMapper.toDTO(categoriaSalva)).thenReturn(dto);

        CategoriaResponseDTO response = service.updateCategoria(1L, request);

        assertThat(response).isEqualTo(dto);
        assertThat(categoria.getNome()).isEqualTo("Novo");
    }

    @Test
    @DisplayName("Deve remover uma categoria do sistema após validar sua existência pelo ID")
    void deveDeletarCategoriaComSucesso() {
        Categoria categoria = new Categoria();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        service.deleteCategoriaById(1L);

        verify(categoriaRepository).delete(categoria);
    }
}