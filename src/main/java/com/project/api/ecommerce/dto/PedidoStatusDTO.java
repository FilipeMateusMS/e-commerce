package com.project.api.ecommerce.dto;

import com.project.api.ecommerce.enums.PedidoStatus;
import jakarta.validation.constraints.NotBlank;

public record PedidoStatusDTO(
        @NotBlank( message = "O status do pedido é obrigatório" )
        PedidoStatus pedidoStatus
        ) {}
