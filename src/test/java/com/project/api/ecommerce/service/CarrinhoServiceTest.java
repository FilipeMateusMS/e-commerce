package com.project.api.ecommerce.service;

import com.project.api.ecommerce.dto.response.CarrinhoResponseDTO;
import com.project.api.ecommerce.dto.response.PageResponse;
import com.project.api.ecommerce.mappers.CarrinhoMapper;
import com.project.api.ecommerce.model.Carrinho;
import com.project.api.ecommerce.repository.CarrinhoItemRepository;
import com.project.api.ecommerce.repository.CarrinhoRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceTest {

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @Mock
    private CarrinhoItemRepository carrinhoItemRepository;

    @Mock
    private CarrinhoMapper carrinhoMapper;

    @InjectMocks
    private CarrinhoService service;

    @Test
    @DisplayName("Deve listar todos os carrinhos de forma paginada com sucesso")
    void deveListarCarrinhosComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);

        Carrinho carrinho = new Carrinho();
        CarrinhoResponseDTO dto = DataDtoFactory.criarCarrinhoResponsePadrao();

        Page<Carrinho> page = new PageImpl<>(List.of(carrinho));

        when(carrinhoRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(carrinhoMapper.toResponseDTO(carrinho)).thenReturn(dto);

        PageResponse<CarrinhoResponseDTO> response = service.getCarrinhos(pageable);

        assertThat(response).isNotNull();
        assertEquals(1, response.content().size());
    }

    @Test
    @DisplayName("Deve buscar e retornar os detalhes de um carrinho específico através do ID")
    void deveBuscarCarrinhoPorIdComSucesso() {
        Carrinho carrinho = new Carrinho();
        CarrinhoResponseDTO dto = DataDtoFactory.criarCarrinhoResponsePadrao();

        when(carrinhoRepository.findById(1L)).thenReturn(Optional.of(carrinho));
        when(carrinhoMapper.toResponseDTO(carrinho)).thenReturn(dto);

        CarrinhoResponseDTO response = service.getCarrinhoById(1L);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    @DisplayName("Deve deletar um carrinho e todos os seus itens vinculados com sucesso")
    void deveDeletarCarrinhoComSucesso() {
        service.deletarCarrinho(1L);

        verify(carrinhoRepository).deleteById(1L);
        verify(carrinhoItemRepository).deleteAllByCarrinhoId(1L);
    }
}