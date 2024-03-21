package com.coremodule.coremodule.repository;

import com.coremodule.coremodule.entities.cart.Cart;
import com.coremodule.coremodule.entities.products.ProductVariation;
import com.coremodule.coremodule.entities.users.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CartRepository extends PagingAndSortingRepository<Cart,ProductRepository> {

    List<Cart> findByCustomerAndProductVariation(Customer customer, ProductVariation productVariation);
    List<Cart>findByCustomer(Customer customer);
}
