package com.project.api.ecommerce.dto.response;

import com.project.api.ecommerce.model.enums.PedidoStatus;
import jakarta.validation.constraints.NotNull;

public record PedidoStatusDTO(
        @NotNull( message = "O status do pedido é obrigatório" )
        PedidoStatus pedidoStatus
        ) {}
