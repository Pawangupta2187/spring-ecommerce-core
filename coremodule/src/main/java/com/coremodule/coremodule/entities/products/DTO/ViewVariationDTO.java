package com.coremodule.coremodule.entities.products.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class ViewVariationDTO {
    private Long id;
    private  String primaryImage;
    private Long quantityAvailable;
    private Long price;
    private Map<String, Object> variation = new HashMap();
    private ViewProductDTO productdetail;


}
