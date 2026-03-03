package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.PedidoControllerOpenApi;
import com.project.api.ecommerce.dto.PedidoDTO;
import com.project.api.ecommerce.dto.PedidoStatusDTO;
import com.project.api.ecommerce.pagination.PageResponse;
import com.project.api.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/pedidos" )
public class PedidoController implements PedidoControllerOpenApi {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PedidoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.getPedido(id));
    }

    @GetMapping("usuario/{idUsuario}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResponse<PedidoDTO>> findPedidosDeUsuario(@PathVariable Long idUsuario, Pageable pageable) {
        return ResponseEntity.ok(pedidoService.getUsuarioPedidos( idUsuario,pageable ) );
    }

    @PostMapping("/usuario/finalizar/{idUsuario}")
    @PreAuthorize("hasRole('USER', 'ADMIN' )")
    public ResponseEntity<PedidoDTO> finalizarPedido(@PathVariable Long idUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.finalizarPedido(idUsuario));
    }

    @PatchMapping("/{idPedido}")
    @PreAuthorize("hasRole( 'ADMIN' )")
    public ResponseEntity<PedidoDTO> alterarStatusPedido(@PathVariable Long idPedido,
                                                         @Valid @RequestBody PedidoStatusDTO pedidoStatusDTO) {
        return ResponseEntity.ok(pedidoService.alterarPedidoStatus(idPedido, pedidoStatusDTO));
    }
}