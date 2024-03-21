package com.coremodule.coremodule.entities.cart.DTO;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class addCartDTO {
    @NotNull(message = "Quantity Can't be Empty")
    @Min(value = 1,message = "Quantity Can't be 0")
    private Long quantity;
}
