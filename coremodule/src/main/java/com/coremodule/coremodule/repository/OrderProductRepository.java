package com.coremodule.coremodule.repository;


import com.coremodule.coremodule.entities.orders.OrderProduct;
import com.coremodule.coremodule.entities.orders.Status;
import com.coremodule.coremodule.entities.products.ProductVariation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderProductRepository extends PagingAndSortingRepository<OrderProduct,Long> {
    List<OrderProduct> findByProductVariation(ProductVariation productVariation);


    @Query("from OrderProduct O where (O.order.orderCreated BETWEEN :date And :currentDate) And O.orderStatus.toStatus=:status")
    List<OrderProduct> findRejectOrderByDate(@Param("date") Date date, @Param("currentDate") Date currentDate,@Param("status") Status status);
}
