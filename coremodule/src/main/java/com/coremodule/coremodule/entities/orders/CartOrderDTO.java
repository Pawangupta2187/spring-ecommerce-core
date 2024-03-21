package com.coremodule.coremodule.entities.orders;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CartOrderDTO {

    @NotNull(message = "address id can't be empty")
    private Long addressId;
    @NotEmpty(message = "Payment Method Can't be Empty")
    @Size(min=1,message = "method not a 0 length")
    private String paymentMethod;
    @Size(min=1)
    private String notes;

    @Min(value = 1,message = "Quantity Can't be 0")
    private Long quantity;
    private
    CartOrderDTO()
    {
        quantity=1L;
        notes="Best Of Luck";
    }
}
