package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.request.CategoriaCreateRequestDTO;
import com.project.api.ecommerce.dto.request.CategoriaUpdateRequestDTO;
import com.project.api.ecommerce.exceptions.BusinessAlertException;
import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.repository.ProdutoRepository;
import com.project.api.ecommerce.dto.response.CategoriaResponseDTO;
import com.project.api.ecommerce.exceptions.ResourceAlreadyExistsException;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.mappers.CategoriaMapper;
import com.project.api.ecommerce.model.Categoria;
import com.project.api.ecommerce.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final ProdutoRepository produtoRepository;

    @Cacheable(value = "categorias", key = "#id")
    public CategoriaResponseDTO findCategoriaById(Long id) {
        return categoriaMapper.toDTO( categoriaRepository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "Categoria não encontrada" ) ) );
    }

    @Cacheable(
            value = "categorias",
            key = "#nome + '-page=' + #pageable.pageNumber + '-size=' + #pageable.pageSize + '-sort=' + #pageable.sort.toString()"
    )
    public PageResponse<CategoriaResponseDTO> findAllByContendoNome( String nome, Pageable pageable ) {
        Page<Categoria> page = ( nome == null || nome.isBlank() )
                        ? categoriaRepository.findAll( pageable )
                        : categoriaRepository.findByNomeContainingIgnoreCase( nome, pageable );

        return PageResponse.of( page.map( categoriaMapper::toDTO ) );
    }

    @CacheEvict(value = "categorias", allEntries = true)
    public CategoriaResponseDTO addCategoria( CategoriaCreateRequestDTO categoriaDTO ) {
        if( categoriaRepository.existsByNome( categoriaDTO.nome() ) )
            throw new ResourceAlreadyExistsException( "Categoria '" + categoriaDTO.nome() + "' já existe" );

        Categoria categoria = new Categoria( categoriaDTO.nome(), obterProdutos( categoriaDTO.produtoIds() ) );
        return categoriaMapper.toDTO( categoriaRepository.save( categoria ) );
    }

    @Transactional
    @Caching(evict = { @CacheEvict(value = "categorias", allEntries = true),
                       @CacheEvict(value = "produtos", allEntries = true ) })
    public void adicionarProdutoNaCategoria( Long categoriaId, Long produtoId ) {
        Categoria categoria = categoriaRepository.findById( categoriaId )
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        Produto produto = produtoRepository.findById( produtoId )
                .orElseThrow( () -> new ResourceNotFoundException( "Produto não encontrado"));

        // O código abaixo não irá funcionar, pois em relacionamentos OneToMany tem que persistir pelo lado do filho
        // categoria.getProdutos().add( produto );
        // categoriaRepository.saveAndFlush( categoria );

        // Deve ser persistido na entidade filha
        produto.setCategoria( categoria );
        produtoRepository.saveAndFlush( produto );
    }

    @Transactional
    @CacheEvict(value = "categorias", allEntries = true)
    public CategoriaResponseDTO updateCategoria( Long id, CategoriaUpdateRequestDTO categoriaDTO ) {
        Categoria categoria = categoriaRepository.findById( id )
                .orElseThrow(() -> new ResourceNotFoundException( "Categoria não encontrada" ) );

        if( categoriaDTO.nome() != null ) // Se enviou o nome
        {
            // verifica se já existe uma categoria que tenha o nome informado
            if( !Objects.equals( categoria.getNome().toLowerCase(), categoriaDTO.nome().toLowerCase() ) )
            {
                if( categoriaRepository.existsByNomeAndIdNot( categoriaDTO.nome(), id ) )
                    throw new ResourceAlreadyExistsException( "Já existe uma categoria com o nome '" + categoriaDTO.nome() + "' " );
            }

            categoria.setNome( categoriaDTO.nome() );
        }

        if( categoriaDTO.produtoIds() != null ) categoria.setProdutos( obterProdutos( categoriaDTO.produtoIds() )  );
        return categoriaMapper.toDTO( categoriaRepository.save( categoria ) );
    }

    private List<Produto> obterProdutos( List<Long> produtoIds ) {
        Set<Long> idsProdutosRecebidos = new HashSet<>( produtoIds ); // Remove duplicatas
        List<Produto> produtos = produtoRepository.findByIdIn( idsProdutosRecebidos );
        if( produtos.size() != idsProdutosRecebidos.size() ) {
            Set<Long> idsExistentes = new HashSet<>();
            produtos.forEach( produto -> idsExistentes.add( produto.getId() ) );
            Set<Long> idsFaltantes = new HashSet<>( idsProdutosRecebidos );
            idsFaltantes.removeAll( idsExistentes );
            throw new ResourceNotFoundException( "Produtos inexistentes: " + idsFaltantes );
        }
        return produtos;
    }

    @CacheEvict(value = "categorias", allEntries = true)
    public void deleteCategoriaById(Long id) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById( id );
        if( categoriaOptional.isEmpty() )
            throw new ResourceNotFoundException( "Categoria não encontrada" );
        Categoria categoria = categoriaOptional.get();
        if( !categoria.getProdutos().isEmpty() )
            throw new BusinessAlertException( "Categoria não pode ser excluida, pois possui produtos relacionados" );

        categoriaRepository.delete( categoria );
    }
}