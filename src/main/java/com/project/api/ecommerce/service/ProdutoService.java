package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.request.ProdutoRequestDTO;
import com.project.api.ecommerce.dto.response.ProdutoResponseDTO;
import com.project.api.ecommerce.dto.filters.ProdutoFilterDTO;
import com.project.api.ecommerce.exceptions.ResourceAlreadyExistsException;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.mappers.ProdutoMapper;
import com.project.api.ecommerce.model.Categoria;
import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.repository.CategoriaRepository;
import com.project.api.ecommerce.repository.ProdutoRepository;
import com.project.api.ecommerce.repository.specs.ProdutoSpecs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoMapper produtoMapper;

    @Cacheable( value = "produtos-item", key = "#id" )
    public ProdutoResponseDTO getProdutoById(Long id) {
        Produto produto = produtoRepository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "Produto não encontrado" ) );
        return produtoMapper.toDTO( produto );
    }

    // Chave composta
    @Cacheable(
            value = "produtos-lista",
            key = "#produtoSearchDTO.toString() + '-' + #pageable.pageNumber + '-' + #pageable.pageSize.toString() + '-' + #pageable.sort.toString()",
            condition = "#pageable.pageNumber == 0"  // Só armazena no Cache a primeira página
    )
    public PageResponse<ProdutoResponseDTO> getAllProdutosFiltered(
            @ModelAttribute ProdutoFilterDTO produtoSearchDTO,
            @PageableDefault( sort= "nome" ) Pageable pageable ) {
        Page<ProdutoResponseDTO> page = produtoRepository.findAll( ProdutoSpecs.buildFromFilter( produtoSearchDTO ), pageable )
                .map( produtoMapper :: toDTO );
        return PageResponse.of( page );
    }

    /*
        Se o produto veio com a categoria verifica se existe a categoria
        Se existir a categoria insere com a categoria existente, senão cria uma categoria
     */
    @Caching( evict = {
                        @CacheEvict(value = "produtos-lista", allEntries = true),
                        @CacheEvict(value = "categorias", allEntries = true) })
    public ProdutoResponseDTO insertProduto(ProdutoRequestDTO produtoDTO ) {
        Categoria categoria = obterOuSalvar( produtoDTO.nomeCategoria() );

        Produto produto = produtoMapper.toProduto( produtoDTO, categoria );
        if( produtoRepository.existsByNomeIgnoreCase( produto.getNome() ) )
            throw new ResourceAlreadyExistsException( "Produto com nome '" + produto.getNome() + "' já existe" );

        return produtoMapper.toDTO( produtoRepository.save( produto ) );
    }

    @Caching(evict = { @CacheEvict(value = "produtos-item", key = "#id" ),
                       @CacheEvict(value = "produtos-lista", allEntries = true ),
                       @CacheEvict(value = "categorias", allEntries = true) })
    public void deleteProdutoById(Long id) {
        if( !produtoRepository.existsById( id ) )
            throw new ResourceNotFoundException( "Produto não encontrado" );
        produtoRepository.deleteById(id);
    }

    @Transactional
    @Caching( evict = { @CacheEvict(value = "produtos-item", key = "#id" ),
                        @CacheEvict(value = "produtos-lista", allEntries = true ),
                        @CacheEvict(value = "categorias", allEntries = true) } )
    public ProdutoResponseDTO updateProduto(Long id, ProdutoRequestDTO produtoRequest ) {
        return produtoRepository.findById( id )
                .map( produtoExistente -> updateProdutoExistente( produtoExistente, produtoRequest ) )
                .map( produtoRepository :: save )
                .map( produtoMapper :: toDTO )
                .orElseThrow( ()-> new ResourceNotFoundException( "Produto não encontrado" ) );
    }

    private Categoria obterOuSalvar( String nomeCategoria ) {
        return Optional.ofNullable( categoriaRepository.findByNome( nomeCategoria ) )
                .orElseGet( () -> categoriaRepository.save( new Categoria( nomeCategoria ) ) );
    }

    private Produto updateProdutoExistente( Produto produtoExistente, ProdutoRequestDTO produtoRequestDTO )
    {
        if( produtoRepository.existsByNomeIgnoreCaseAndIdNot( produtoRequestDTO.nome(), produtoExistente.getId() ) )
            throw new ResourceAlreadyExistsException( "Produto com nome '" + produtoRequestDTO.nome() + "' já existe" );

        if( produtoRequestDTO.nome() != null && !produtoRequestDTO.nome().isBlank() )
            produtoExistente.setNome( produtoRequestDTO.nome() );
        if( produtoRequestDTO.marca() != null && !produtoRequestDTO.marca().isBlank() )
            produtoExistente.setMarca( produtoRequestDTO.marca() );
        if( produtoRequestDTO.descricao() != null && !produtoRequestDTO.descricao().isBlank() )
            produtoExistente.setDescricao( produtoRequestDTO.descricao() );
        if( produtoRequestDTO.preco() != null )
            produtoExistente.setPrecoUnitario( produtoRequestDTO.preco() );
        if( produtoRequestDTO.quantidade() != null )
            produtoExistente.setQuantidade( produtoRequestDTO.quantidade() );

        Categoria categoria = obterOuSalvar( produtoRequestDTO.nomeCategoria() );
        produtoExistente.setCategoria( categoria );

        return produtoExistente;
    }
}