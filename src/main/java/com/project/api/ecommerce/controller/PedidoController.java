package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.PedidoControllerOpenApi;
import com.project.api.ecommerce.dto.response.PedidoResponseDTO;
import com.project.api.ecommerce.dto.response.PedidoStatusDTO;
import com.project.api.ecommerce.commom.pagination.PageResponse;
import com.project.api.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/pedidos" )
@RequiredArgsConstructor
public class PedidoController implements PedidoControllerOpenApi {

    private final PedidoService pedidoService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN' )")
    public ResponseEntity<PedidoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.getPedido(id));
    }

    @GetMapping("/usuario")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PageResponse<PedidoResponseDTO>> findPedidosDeUsuario(Pageable pageable) {
        return ResponseEntity.ok(pedidoService.getUsuarioPedidos( pageable ) );
    }

    @PostMapping("/usuario/finalizar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN' )")
    public ResponseEntity<PedidoResponseDTO> finalizarPedido() {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.finalizarPedido());
    }

    @PatchMapping("/{idPedido}")
    @PreAuthorize("hasRole( 'ADMIN' )")
    public ResponseEntity<PedidoResponseDTO> alterarStatusPedido(@PathVariable Long idPedido,
                                                                 @Valid @RequestBody PedidoStatusDTO pedidoStatusDTO) {
        return ResponseEntity.ok(pedidoService.alterarPedidoStatus(idPedido, pedidoStatusDTO));
    }
}