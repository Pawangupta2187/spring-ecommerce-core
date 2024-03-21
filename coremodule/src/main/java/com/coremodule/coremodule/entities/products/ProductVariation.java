package com.coremodule.coremodule.entities.products;

//import com.coremodule.coremodule.Auditing.Auditable;

import com.coremodule.coremodule.Auditing.Auditable;
import com.coremodule.coremodule.entities.JSONObjectConverter;
import com.coremodule.coremodule.entities.cart.Cart;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter

public class ProductVariation extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;
    private Long quantityAvailable;
    private Long price;
    private Boolean isActive=true;
    private String primaryImage;

    @OneToMany(mappedBy ="productVariation")
    private Set<Cart> carts;


    @Column(columnDefinition = "TEXT")
    @Convert(converter= JSONObjectConverter.class)
//    @Type( type = "json" )
//    @Column( columnDefinition = "json" )
    private JSONObject metadata;
    public ProductVariation() {
        isActive = true;
    }


    @Override
    public String toString() {
        return
                "id=" + id +
                "\n quantityAvailable=" + quantityAvailable +
                "\n price=" + price +
                "\n isActive=" + isActive +
                "\n primaryImage='" + primaryImage + '\'' +
                "\n metadata=" + metadata ;
    }
}
