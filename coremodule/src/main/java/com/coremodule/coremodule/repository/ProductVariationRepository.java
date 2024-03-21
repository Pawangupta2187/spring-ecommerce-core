package com.coremodule.coremodule.repository;

import com.coremodule.coremodule.entities.products.Product;
import com.coremodule.coremodule.entities.products.ProductVariation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductVariationRepository extends CrudRepository<ProductVariation,Long> {

List<ProductVariation> findByProduct(Product product, Pageable pageable);
}
