package com.project.api.ecommerce.controller.openapi;

import com.project.api.ecommerce.dto.response.PedidoResponseDTO;
import com.project.api.ecommerce.dto.response.PedidoStatusDTO;
import com.project.api.ecommerce.commom.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Pedidos", description = "Endpoints para gestão de pedidos e fluxo de checkout")
public interface PedidoControllerOpenApi {

    @Operation(summary = "Busca pedido por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não localizado", content = @Content)
    })
    ResponseEntity<PedidoResponseDTO> findById(@Parameter(description = "ID do pedido", example = "1") Long id);

    @Operation(summary = "Lista pedidos do usuário autenticado com paginação")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    ResponseEntity<PageResponse<PedidoResponseDTO>> findPedidosDeUsuario(
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "Finaliza o carrinho atual do usuário logado",
            description = "Transforma os itens do carrinho em um pedido fechado.")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso" )
    ResponseEntity<PedidoResponseDTO> finalizarPedido();

    @Operation(summary = "Atualiza o status de um pedido", description = "Permite transições como PENDENTE -> PAGO ou CANCELADO.")
    @ApiResponse(responseCode = "200", description = "Status atualizado")
    ResponseEntity<PedidoResponseDTO> alterarStatusPedido(
            @PathVariable Long idPedido,
            @RequestBody PedidoStatusDTO pedidoStatusDTO
    );
}