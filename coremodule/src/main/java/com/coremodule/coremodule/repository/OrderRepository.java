package com.coremodule.coremodule.repository;

import com.coremodule.coremodule.entities.orders.Order;
import com.coremodule.coremodule.entities.users.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order,Long> {

    Order findByCustomerAndId(Customer customer,Long orderId);
}
