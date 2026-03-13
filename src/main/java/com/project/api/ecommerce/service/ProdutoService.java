package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.request.ProdutoRequestDTO;
import com.project.api.ecommerce.dto.response.ProdutoResponseDTO;
import com.project.api.ecommerce.dto.filters.ProdutoFilterDTO;
import com.project.api.ecommerce.exceptions.ResourceAlreadyExistsException;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.mappers.ProdutoMapper;
import com.project.api.ecommerce.model.Categoria;
import com.project.api.ecommerce.model.Produto;
import com.project.api.ecommerce.pagination.PageResponse;
import com.project.api.ecommerce.repository.CategoriaRepository;
import com.project.api.ecommerce.repository.ProdutoRepository;
import com.project.api.ecommerce.specifications.ProdutoSpecs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoResponseDTO getProdutoById(Long id) {
        Produto produto = produtoRepository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "Produto não encontrado" ) );
        return produtoMapper.toDTO( produto );
    }

    public Produto getProdutoEntity(Long id) {
        return produtoRepository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "Produto não encontrado" ) );
    }

    public PageResponse<ProdutoResponseDTO> getAllProdutosFiltered(
            ProdutoFilterDTO produtoSearchDTO,
            Pageable pageable ) {
        Page<ProdutoResponseDTO> page = produtoRepository.findAll( ProdutoSpecs.buildFromFilter( produtoSearchDTO ), pageable )
                .map( produtoMapper :: toDTO );
        return PageResponse.of( page );
    }

    /*
        Se o produto veio com a categoria verifica se existe a categoria
        Se existir a categoria insere com a categoria existente, senão cria uma categoria
     */
    public ProdutoResponseDTO insertProduto(ProdutoRequestDTO produtoDTO ) {
        Categoria categoria = obterOuSalvar( produtoDTO.nomeCategoria() );

        Produto produto = produtoMapper.toProduto( produtoDTO, categoria );
        if( produtoRepository.existsByNomeIgnoreCase( produto.getNome() ) )
            throw new ResourceAlreadyExistsException( "Produto com nome '" + produto.getNome() + "' já existe" );

        return produtoMapper.toDTO( produtoRepository.save( produto ) );
    }

    private Categoria obterOuSalvar( String nomeCategoria ) {
        return Optional.ofNullable( categoriaRepository.findByNome( nomeCategoria ) )
                .orElseGet( () -> categoriaRepository.save( new Categoria( nomeCategoria ) ) );
    }

    public void deleteProdutoById(Long id) {
        if( !produtoRepository.existsById( id ) )
            throw new ResourceNotFoundException( "Produto não encontrado" );
        produtoRepository.deleteById(id);
    }

    public ProdutoResponseDTO updateProduto(Long id, ProdutoRequestDTO produtoRequest ) {
        return produtoRepository.findById( id )
                .map( produtoExistente -> updateProdutoExistente( produtoExistente, produtoRequest ) )
                .map( produtoRepository :: save )
                .map( produtoMapper :: toDTO )
                .orElseThrow( ()-> new ResourceNotFoundException( "Produto não encontrado" ) );
    }

    public Produto updateProdutoExistente( Produto produtoExistente, ProdutoRequestDTO produtoRequestDTO )
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