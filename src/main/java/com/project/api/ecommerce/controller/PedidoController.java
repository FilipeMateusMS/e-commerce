package com.project.api.ecommerce.controller;

import com.project.api.ecommerce.controller.openapi.PedidoControllerOpenApi;
import com.project.api.ecommerce.dto.PedidoDTO;
import com.project.api.ecommerce.dto.PedidoStatusDTO;
import com.project.api.ecommerce.pagination.PageResponse;
import com.project.api.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/pedidos" )
public class PedidoController implements PedidoControllerOpenApi {

    private final PedidoService pedidoService;

    private static final Logger logger = LoggerFactory.getLogger( PedidoController.class );

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.getPedido(id));
    }

    @GetMapping("usuario/{idUsuario}")
    public ResponseEntity<PageResponse<PedidoDTO>> findPedidosDeUsuario(@PathVariable Long idUsuario, Pageable pageable) {
        return ResponseEntity.ok(pedidoService.getUsuarioPedidos( idUsuario,pageable ) );
    }

    @PostMapping("/usuario/finalizar/{idUsuario}")
    public ResponseEntity<PedidoDTO> finalizarPedido(@PathVariable Long idUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.finalizarPedido(idUsuario));
    }

    @PatchMapping("/{idPedido}")
    public ResponseEntity<PedidoDTO> alterarStatusPedido(@PathVariable Long idPedido,
                                                         @Valid @RequestBody PedidoStatusDTO pedidoStatusDTO) {
        return ResponseEntity.ok(pedidoService.alterarPedidoStatus(idPedido, pedidoStatusDTO));
    }
}