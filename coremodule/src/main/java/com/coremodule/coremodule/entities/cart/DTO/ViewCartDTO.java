package com.coremodule.coremodule.entities.cart.DTO;

import com.coremodule.coremodule.entities.products.DTO.ViewVariationDTO;
import lombok.Data;

@Data
public class ViewCartDTO {
    private Long quantity;
    private ViewVariationDTO variation;
    }
