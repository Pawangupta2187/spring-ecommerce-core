package com.coremodule.coremodule.entities.products.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerViewProductDTO {
    private String description;
    private String brand;
    private Boolean isCancellable;
    private Boolean isReturnable;
    private Boolean isDelete;
    private HashMap<Object,Object> categoryDetails;
    private List<ViewVariationDTO> variations=new ArrayList<>();
}
