package com.coremodule.coremodule.entities.orders;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ViewOrderProductDTO {
    private Long id;
    private Long quantity;
    private Long price;
    private Map<String, Object> metadata = new HashMap();
    //private ProductVariation productVariation;
    private OrderStatus orderStatus;
}
