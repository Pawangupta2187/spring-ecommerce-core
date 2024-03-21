package com.coremodule.coremodule.entities.products.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class UpdateProductDTO {
    @NotEmpty(message = "Name Can't be Empty")
    private String name;
    private String description;
    private Boolean isCancellable;
    private Boolean isReturnable;
}
