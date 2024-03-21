package com.coremodule.coremodule.controllers;

import com.coremodule.coremodule.service.CartService;
import com.coremodule.coremodule.entities.cart.DTO.ViewCartDTO;
import com.coremodule.coremodule.entities.cart.DTO.addCartDTO;
import com.coremodule.coremodule.exception.SuccessResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/cart")
@RestController


public class CartController {

    @Autowired
    CartService cartService;


    @PostMapping("/{variationId}")
    public ResponseEntity<SuccessResponse> addToCart(@RequestBody @Valid addCartDTO cart, @PathVariable Long variationId) {
        return cartService.addToCart(cart.getQuantity(), variationId);
    }

    @GetMapping("/")
    public List<ViewCartDTO> allCartProduct() {
        return cartService.allCartProduct();
    }

    @DeleteMapping("/")
    public ResponseEntity<SuccessResponse> emptyCart() {
        return cartService.emptyCart();
    }

    @DeleteMapping("/{variationId}")
    public ResponseEntity<SuccessResponse> deleteCart(@PathVariable Long variationId) {
        return cartService.deleteCart(variationId);
    }

    @PutMapping("/{variationId}")
    public ResponseEntity<SuccessResponse> updateCart(@RequestBody @Valid addCartDTO cart, @PathVariable Long variationId) {
        return cartService.updateCart(cart.getQuantity(), variationId);
    }


}
