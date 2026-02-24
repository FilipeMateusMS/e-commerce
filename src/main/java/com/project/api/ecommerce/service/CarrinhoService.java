package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.CarrinhoResponseDTO;
import com.project.api.ecommerce.exceptions.ResourceNotFoundException;
import com.project.api.ecommerce.mappers.CarrinhoMapper;
import com.project.api.ecommerce.model.Carrinho;
import com.project.api.ecommerce.pagination.PageResponse;
import com.project.api.ecommerce.repository.CarrinhoItemRepository;
import com.project.api.ecommerce.repository.CarrinhoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final CarrinhoItemRepository carrinhoItemRepository;
    private final CarrinhoMapper carrinhoMapper;

    private static final Logger logger = LoggerFactory.getLogger( CarrinhoService.class );

    public CarrinhoService( CarrinhoRepository carrinhoRepository, CarrinhoItemRepository carrinhoItemRepository, CarrinhoMapper carrinhoMapper) {
        this.carrinhoRepository = carrinhoRepository;
        this.carrinhoItemRepository = carrinhoItemRepository;
        this.carrinhoMapper = carrinhoMapper;
    }

    public PageResponse<CarrinhoResponseDTO> getCarrinhos( Pageable pageable ) {
        Page<CarrinhoResponseDTO> dtoPage = carrinhoRepository.findAll( pageable )
                .map( carrinhoMapper::toResponseDTO );
        return PageResponse.of( dtoPage );
    }

    public CarrinhoResponseDTO getCarrinhoById( Long id )
    {
        return carrinhoMapper.toResponseDTO( carrinhoRepository.findById( id ).orElseThrow( ()->
                new ResourceNotFoundException( "Carrinho não encontrado" ) ) );
    }

    @Transactional
    public void deletarCarrinho( Long id ){
        carrinhoRepository.deleteById( id );
        carrinhoItemRepository.deleteAllByCarrinhoId( id );
    }
}