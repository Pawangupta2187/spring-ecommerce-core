package com.coremodule.coremodule.repository;

import com.coremodule.coremodule.entities.products.Product;
import com.coremodule.coremodule.entities.users.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product,Long> {

    @Query("from Product P where P.name=:productName And P.brand=:brand And P.category.id=:catId And P.seller.id=:sellerId")
    List<Product> checkifProductExist(@Param("productName") String productName, @Param("brand") String brand,@Param("catId") Long catId,@Param("sellerId") Long sellerId);
List<Product>findBySeller(Seller seller, Pageable pageable);

@Query("from Product P where (P.name LIKE %:productName% Or P.brand LIKE %:productBrand%) And P.category.id=:categoryId")
List<Product>findSimilarProduct(@Param("productName") String productName,@Param("productBrand") String productBrand,@Param("categoryId") Long categoryId,Pageable pageable);

}
