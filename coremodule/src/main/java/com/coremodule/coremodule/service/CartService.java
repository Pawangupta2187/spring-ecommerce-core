package com.coremodule.coremodule.service;

import com.coremodule.coremodule.entities.cart.Cart;
import com.coremodule.coremodule.entities.cart.CartKey;
import com.coremodule.coremodule.entities.cart.DTO.ViewCartDTO;
import com.coremodule.coremodule.entities.products.DTO.ViewVariationDTO;
import com.coremodule.coremodule.entities.products.ProductVariation;
import com.coremodule.coremodule.entities.users.Customer;
import com.coremodule.coremodule.exception.BadRequestException;
import com.coremodule.coremodule.exception.ConflictException;
import com.coremodule.coremodule.exception.NotFoundException;
import com.coremodule.coremodule.exception.SuccessResponse;
import com.coremodule.coremodule.repository.CartRepository;
import com.coremodule.coremodule.repository.ProductVariationRepository;
import com.coremodule.coremodule.repository.RegisterCustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    @Autowired
    RegisterCustomerRepository customerRepository;

    @Autowired
    ProductVariationRepository productVariationRepository;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    ModelMapper mm;
    @Autowired
    LoggedInService loggedInService;

    @Autowired
    OrderService orderService;

    public ResponseEntity<SuccessResponse> addToCart(Long quantity, Long variationId) {
        String email = loggedInService.getLoggedInUser();
        Customer customer = customerRepository.findCustomerByemailId(email);
        Optional<ProductVariation> productVariation = productVariationRepository.findById(variationId);
        orderService.productIsvalid(productVariation.get());
       // Customer customer = customers.get(0);
        if (productVariation.get().getQuantityAvailable() < quantity)
            throw new NotFoundException("only " + productVariation.get().getQuantityAvailable() + " Quantity Available");
        List<Cart> carts = cartRepository.findByCustomerAndProductVariation(customer, productVariation.get());
        if (carts.size() != 0) {
            throw new ConflictException("Product already in cart go for update");
        } else {
            try {
                cartRepository
                        .save(new Cart
                                (new CartKey(customer.getId(), productVariation.get().getId()),
                                        customer,
                                        productVariation.get(),
                                        quantity
                                ));
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new BadRequestException(ex.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Product Succesfully Added to the Cart"));

    }

    public List<ViewCartDTO> allCartProduct() {
        String email = loggedInService.getLoggedInUser();
        Customer customer = customerRepository.findCustomerByemailId(email);
        Set<Cart> carts = customer.getCarts();
        if (carts.size() == 0)
            throw new NotFoundException("Cart is Empty");
        List<ViewCartDTO> viewCartDTOList = new ArrayList<>();
        for (Cart cart : carts) {
            ViewCartDTO cartDTO = new ViewCartDTO();
            cartDTO.setQuantity(cart.getQuantity());
            ViewVariationDTO variationDTO = new ViewVariationDTO();
            mm.map(cart.getProductVariation(), variationDTO);
            //variationDTO.setProductdetail(viewProduct(productVariation.get().getProduct().getId(), email));
            Map<String, Object> metadata = cart.getProductVariation().getMetadata().toMap();
            variationDTO.setVariation(metadata);
            cartDTO.setVariation(variationDTO);
            viewCartDTOList.add(cartDTO);
        }
        return viewCartDTOList;
    }

    public ResponseEntity<SuccessResponse> deleteCart(Long variationId) {
        String email = loggedInService.getLoggedInUser();
        Customer customer = customerRepository.findCustomerByemailId(email);
        Optional<ProductVariation> productVariation = productVariationRepository.findById(variationId);
        orderService.productIsvalid(productVariation.get());
        List<Cart> carts = cartRepository.findByCustomerAndProductVariation(customer, productVariation.get());
        if (carts.size() == 0)
            throw new NotFoundException("Cart's Product Not found");
        Cart cart = carts.get(0);
        if (cart.getQuantity() == 1) {
            try {
                cartRepository.delete(cart);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new BadRequestException(ex.getMessage());
            }
        } else {
            cart.setQuantity(cart.getQuantity() - 1);
            try {
                cartRepository.save(cart);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new BadRequestException(ex.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Deleted"));
    }

    public ResponseEntity<SuccessResponse> emptyCart() {
        String email = loggedInService.getLoggedInUser();
        Customer customer = customerRepository.findCustomerByemailId(email);
         List<Cart> carts = cartRepository.findByCustomer(customer);
        if (carts.size() != 0) {
            for (Cart cart : carts) {
                try {
                    System.out.println(cart.getCustomer().getFirstName() + "123456");
                    cartRepository.delete(cart);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new BadRequestException(ex.getMessage());
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Cart Empty Now"));
    }

    public ResponseEntity<SuccessResponse> updateCart(Long quantity, Long variationId) {
        String email = loggedInService.getLoggedInUser();
        Customer customer = customerRepository.findCustomerByemailId(email);
        Optional<ProductVariation> productVariation = productVariationRepository.findById(variationId);
        orderService.productIsvalid(productVariation.get());
         List<Cart> carts = cartRepository.findByCustomerAndProductVariation(customer, productVariation.get());
        if (carts.size() == 0)
            throw new NotFoundException("product not found in cart");
        Cart cart = carts.get(0);
        if (productVariation.get().getQuantityAvailable() < quantity + cart.getQuantity())
            throw new NotFoundException("only " + productVariation.get().getQuantityAvailable() + " Quantity Available");
        cart.setQuantity(cart.getQuantity() + quantity);
        try {
            cartRepository.save(carts.get(0));
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Cart updated Succesfuly"));
    }

}
