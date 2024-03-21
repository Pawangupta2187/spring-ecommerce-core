package com.coremodule.coremodule.entities.products.DTO;

import com.coremodule.coremodule.entities.products.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class AddProductVariationDTO {
//    Product Id
//

 // HashMetadata (field-value map)
    @NotEmpty(message = "primary image cant be Empty")
    private  String primaryImage;
    @NotNull(message = "Quantiy can't be Empty")
    private Long quantityAvailable;
    @NotNull(message = "Price can't be Empty")
    private Long price;
    private Product product;
    private Boolean isActive;
    private Map<Object, Object> metadata = new HashMap();

    public AddProductVariationDTO(Map<Object, Object> fieldValuemap, String primaryImage, Long quantityAvailable, Long price, Product product) {
        this.metadata = fieldValuemap;
        this.primaryImage = primaryImage;
        this.quantityAvailable = quantityAvailable;
        this.price = price;
        this.product = product;
    }
}
