package com.coremodule.coremodule.controllers;

import com.coremodule.coremodule.entities.orders.*;
import com.coremodule.coremodule.entities.users.PagingDefinationDTO;
import com.coremodule.coremodule.exception.SuccessResponse;
import com.coremodule.coremodule.repository.AddressRepository;
import com.coremodule.coremodule.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/order")
@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    AddressRepository addressRepository;

    //customer
    @PostMapping("/orderfromcart")
    public ResponseEntity<SuccessResponse> orderFromCart(@RequestBody @Valid CartOrderDTO cartOrderDTO) {
        return orderService.orderFromCart(cartOrderDTO);
    }

    @PostMapping("/orderfromcart/{variationId}")
    public ResponseEntity<SuccessResponse> partialOrderCart(@PathVariable Long variationId, @RequestBody @Valid CartOrderDTO cartOrderDTO) {
        return orderService.partialOrderCart(variationId, cartOrderDTO);
    }

    @PostMapping("/{variationId}")
    public ResponseEntity<SuccessResponse> order(@PathVariable Long variationId, @RequestBody @Valid CartOrderDTO cartOrderDTO) {
        return orderService.order(variationId, cartOrderDTO);
    }

    @PutMapping("/cancelorder/{orderProductId}")
    public ResponseEntity<SuccessResponse> cancelOrder(@PathVariable Long orderProductId) {
        return orderService.cancelOrder(orderProductId);
    }

    @PutMapping("/returnorder/{orderProductId}")
    public ResponseEntity<SuccessResponse> returnOrder(@PathVariable Long orderProductId) {
        return orderService.returnOrder(orderProductId);
    }


    @GetMapping("/{orderId}")
    public ViewOrderDTO getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("/")
    public Page<ViewOrderDTO> getAllOrder(HttpServletRequest request, @RequestBody(required = false) PagingDefinationDTO pagingDefination) {
        if (pagingDefination == null)
            pagingDefination = new PagingDefinationDTO();
        return orderService.getAllOrder(request, pagingDefination);
    }


    //seller

    @GetMapping("/seller/allorder")
    public Page<ViewOrderProductDTO> getSellerAllOrder(@RequestBody(required = false) PagingDefinationDTO pagingDefination) {
        if (pagingDefination == null)
            pagingDefination = new PagingDefinationDTO();
        return orderService.getSellerAllOrder(pagingDefination);
    }

    @PatchMapping("/seller/status/{orderProductId}")
    public ResponseEntity<SuccessResponse> updateStatus(@PathVariable @Valid Long orderProductId, @RequestBody OrderStatusDTO orderStatus) {
        return orderService.updateStatus(orderProductId, orderStatus);
    }


    //admin
    @GetMapping("/admin/allorders")
    public Page<ViewOrderDTO> getAllOrders(@RequestBody(required = false) PagingDefinationDTO pagingDefination) {

        if (pagingDefination == null)
            pagingDefination = new PagingDefinationDTO();
        return orderService.getAllOrders(pagingDefination);
    }

    @PatchMapping("/admin/status/{orderProductId}")
    public ResponseEntity<SuccessResponse> changeStatus(HttpServletRequest request, @PathVariable Long orderProductId, @RequestBody @Valid OrderStatusDTO orderStatus) {
        return orderService.changeStatus(orderProductId, orderStatus);

    }
}
