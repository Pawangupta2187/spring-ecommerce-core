package com.coremodule.coremodule.entities.orders;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderStatusDTO {

    @NotNull(message = "From status Cant be empty")
    private Status fromStatus;
    @NotNull(message = "To status Cant be empty")
    private Status toStatus;
}
