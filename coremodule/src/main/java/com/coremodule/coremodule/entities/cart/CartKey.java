package com.coremodule.coremodule.entities.cart;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
public class CartKey implements Serializable {
    private Long customerId;
    private Long variationId;

    public CartKey(Long customerId, Long variationId) {
        this.customerId = customerId;
        this.variationId = variationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartKey cartKey = (CartKey) o;
        return Objects.equals(customerId, cartKey.customerId) && Objects.equals(variationId, cartKey.variationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, variationId);
    }
}
