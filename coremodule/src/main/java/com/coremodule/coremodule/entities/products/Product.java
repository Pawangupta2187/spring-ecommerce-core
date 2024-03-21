package com.coremodule.coremodule.entities.products;

//import com.coremodule.coremodule.Auditing.Auditable;

import com.coremodule.coremodule.Auditing.Auditable;
import com.coremodule.coremodule.entities.categories.Category;
import com.coremodule.coremodule.entities.users.Seller;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Product extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotEmpty(message = "Please Add Product Name")
    private String name;

    private String description;
    @NotEmpty(message = "Please Add Brand Name")
    private String brand;
    private Boolean isActive=false;
    private Boolean isCancellable=false;
    private Boolean isReturnable=false;
    private Boolean isDelete=false;
    @ManyToOne
    @JoinColumn(name="seller_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Set<ProductVariation> productVariations;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Set<ProductReview>productReviews;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(brand, product.brand) && Objects.equals(isActive, product.isActive) && Objects.equals(isCancellable, product.isCancellable) && Objects.equals(isReturnable, product.isReturnable) && Objects.equals(isDelete, product.isDelete) && Objects.equals(seller, product.seller) && Objects.equals(category, product.category) && Objects.equals(productVariations, product.productVariations) && Objects.equals(productReviews, product.productReviews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, brand, isActive, isCancellable, isReturnable, isDelete, seller, category, productVariations, productReviews);
    }

    public void addProductVariation(ProductVariation productVariation)
    {
        if(productVariation!=null)
        {
            if(productVariations==null)
                productVariations=new HashSet<>();
            productVariations.add(productVariation);
            productVariation.setProduct(this);
        }

    }
}
