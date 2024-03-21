package com.coremodule.coremodule.entities.cart;

//import com.coremodule.coremodule.Auditing.Auditable;


import com.coremodule.coremodule.Auditing.Auditable;
import com.coremodule.coremodule.entities.products.ProductVariation;
import com.coremodule.coremodule.entities.users.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cart extends Auditable<String> {

    @EmbeddedId
    CartKey id;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name="customer_id")
    private Customer customer;

    @ManyToOne
    @MapsId("variationId")
    @JoinColumn(name="productvariation_id")
    private ProductVariation productVariation;

    @NotNull(message = "Quantity Can't be Empty")
    @Min(value = 1,message = "Quantity Can't be 0")
    private Long quantity;

    public Cart(CartKey id, Customer customer, ProductVariation productVariation, Long quantity) {
        this.id = id;
        this.customer = customer;
        this.productVariation = productVariation;
        this.quantity = quantity;
    }
}
