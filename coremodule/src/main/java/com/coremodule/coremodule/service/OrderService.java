package com.coremodule.coremodule.service;

import com.coremodule.coremodule.entities.cart.Cart;
import com.coremodule.coremodule.entities.orders.*;
import com.coremodule.coremodule.entities.products.Product;
import com.coremodule.coremodule.entities.products.ProductVariation;
import com.coremodule.coremodule.entities.users.Address;
import com.coremodule.coremodule.entities.users.Customer;
import com.coremodule.coremodule.entities.users.PagingDefinationDTO;
import com.coremodule.coremodule.entities.users.Seller;
import com.coremodule.coremodule.exception.BadRequestException;
import com.coremodule.coremodule.exception.NotFoundException;
import com.coremodule.coremodule.exception.SuccessResponse;
import com.coremodule.coremodule.repository.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RegisterCustomerRepository customerRepository;

    @Autowired
    RegisterSellerRepository sellerRepository;

    @Autowired
    AddressRepository addressRepository;


    @Autowired
    ModelMapper mm;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductVariationRepository productVariationRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    LoggedInService loggedInService;

    public ResponseEntity<SuccessResponse> orderFromCart(CartOrderDTO cartOrderDTO) {
        String email = loggedInService.getLoggedInUser();
        Customer customer = customerRepository.findCustomerByemailId(email);
        Set<Cart> carts = customer.getCarts();
        if (carts.size() == 0)
            throw new NotFoundException("Cart is Empty");
        Optional<Address> address = addressRepository.findById(cartOrderDTO.getAddressId());
        if (address.isEmpty())
            throw new NotFoundException("address not find");
        Long totalamount = 0L;
        Set<OrderProduct> orderProductList = new HashSet<>();
        for (Cart cart : carts) {
            ProductVariation productVariation = cart.getProductVariation();
            productIsvalid(productVariation);

            if (productVariation.getQuantityAvailable() < cart.getQuantity())
                throw new NotFoundException("only " + productVariation.getQuantityAvailable() + " Quantity Available");

            OrderProduct orderProduct = CreateOrderProduct(cartOrderDTO.getNotes(), cart.getQuantity(), productVariation);
            totalamount += orderProduct.getPrice();
            orderProductList.add(orderProduct);
        }
        Order order = createOrder(cartOrderDTO, address.get(), totalamount);
        order.setOrderProducts(orderProductList);
        customer.addOrder(order);
        try {
            customer.setCarts(null);
            customerRepository.save(customer);
            for (Cart cart : carts) {
                cartRepository.delete(cart);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Your order is created"));
    }




    public ResponseEntity<SuccessResponse> partialOrderCart(Long variationId, CartOrderDTO cartOrderDTO) {
        String email = loggedInService.getLoggedInUser();
       Customer customer = customerRepository.findCustomerByemailId(email);
        Optional<ProductVariation> productVariation = productVariationRepository.findById(variationId);
        productIsvalid(productVariation.get());
    //    Customer customer = customers.get(0);
        List<Cart> carts = cartRepository.findByCustomerAndProductVariation(customer, productVariation.get());
        if (carts.size() == 0)
            throw new NotFoundException("Variation Not Found in Cart");
        Cart cart = carts.get(0);
        Optional<Address> address = addressRepository.findById(cartOrderDTO.getAddressId());
        if (address.isEmpty())
            throw new NotFoundException("address not find");
        OrderProduct orderProduct = CreateOrderProduct(cartOrderDTO.getNotes(), cart.getQuantity(), productVariation.get());
        Order order = createOrder(cartOrderDTO, address.get(), cart.getQuantity() * productVariation.get().getPrice());
        order.addOrderProducts(orderProduct);
        customer.addOrder(order);
        try {
            customerRepository.save(customer);
            cartRepository.delete(cart);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Your Order is Created Successfully"));
    }


    public ResponseEntity<SuccessResponse> order(Long variationId, CartOrderDTO cartOrderDTO) {
        String email = loggedInService.getLoggedInUser();
        Customer customer = customerRepository.findCustomerByemailId(email);
     //   Customer customer = customers.get(0);
        Optional<ProductVariation> productVariation = productVariationRepository.findById(variationId);
        productIsvalid(productVariation.get());
        Optional<Address> address = addressRepository.findById(cartOrderDTO.getAddressId());
        if (address.isEmpty())
            throw new NotFoundException("address not find");
        OrderProduct orderProduct = CreateOrderProduct(cartOrderDTO.getNotes(), cartOrderDTO.getQuantity(), productVariation.get());
        Order order = createOrder(cartOrderDTO, address.get(), cartOrderDTO.getQuantity() * productVariation.get().getPrice());
        order.addOrderProducts(orderProduct);
        customer.addOrder(order);
        try {
            customerRepository.save(customer);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Your Order is Created Successfully"));

    }


    public ResponseEntity<SuccessResponse> cancelOrder(Long orderProductId) {
        String email = loggedInService.getLoggedInUser();
        Customer customer = customerRepository.findCustomerByemailId(email);

        Optional<OrderProduct> orderProduct = orderProductRepository.findById(orderProductId);
        if (orderProduct.isEmpty())
            throw new NotFoundException("Order Product Not Found");
        if (orderProduct.get().getOrderStatus().getToStatus() != Status.ORDER_CONFIRMED &&
                orderProduct.get().getOrderStatus().getToStatus() != Status.ORDER_SHIPPED)
            throw new BadRequestException("Order Product is Not in Valid State");
        Order order = orderRepository.findByCustomerAndId(customer, orderProduct.get().getOrder().getId());
        if (order == null)
            throw new NotFoundException("Customer's Order is Not Found");
        OrderStatus orderStatus = orderProduct.get().getOrderStatus();
        orderStatus.setFromStatus(orderProduct.get().getOrderStatus().getToStatus());
        orderStatus.setToStatus(Status.CANCELLED);
        orderProduct.get().setOrderStatus(orderStatus);
        try {
            orderProductRepository.save(orderProduct.get());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Order's Product is Cancelled"));

    }

    public ResponseEntity<SuccessResponse> returnOrder(Long orderProductId) {
        String email = loggedInService.getLoggedInUser();
        Customer customer = customerRepository.findCustomerByemailId(email);
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(orderProductId);
        if (orderProduct.isEmpty())
            throw new NotFoundException("Order Product Not Found");
        Order order = orderRepository.findByCustomerAndId(customer, orderProduct.get().getOrder().getId());
        if (order == null)
            throw new NotFoundException("Customer's Order is Not Found");
        System.out.println(orderProduct.get().getOrderStatus().getToStatus() + "deliver");
        if (orderProduct.get().getOrderStatus().getToStatus() != Status.DELIVERED)
            throw new BadRequestException("Order Product is Not in Valid State");
        OrderStatus orderStatus = orderProduct.get().getOrderStatus();
        orderStatus.setFromStatus(orderProduct.get().getOrderStatus().getToStatus());
        orderStatus.setToStatus(Status.RETURN_REQUESTED);
        orderProduct.get().setOrderStatus(orderStatus);
        try {
            orderProductRepository.save(orderProduct.get());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Product return request is in Process Wait for Approved"));
    }

    public ViewOrderDTO getOrder(Long orderId) {
        String email = loggedInService.getLoggedInUser();
        Customer customer =customerRepository.findCustomerByemailId(email);
        if (customer == null)
            throw new NotFoundException("Customer not found");
        Order order = orderRepository.findByCustomerAndId(customer, orderId);
        if (order == null)
            throw new NotFoundException("Customer's Order is Not Found");
        List<ViewOrderProductDTO> orderProductList = new ArrayList<>();
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            ViewOrderProductDTO viewOrderProductDTO = new ViewOrderProductDTO();
            mm.map(orderProduct, viewOrderProductDTO);
            viewOrderProductDTO.setMetadata(orderProduct.getMetaData().toMap());
            orderProductList.add(viewOrderProductDTO);
        }
        ViewOrderDTO viewOrderDTO = new ViewOrderDTO();
        mm.map(order, viewOrderDTO);
        viewOrderDTO.setOrderProducts(orderProductList);
        return viewOrderDTO;
    }

    public Page<ViewOrderDTO> getAllOrder(HttpServletRequest request, PagingDefinationDTO pagingDefinationDTO) {
        Pageable pageable = PageRequest.of(pagingDefinationDTO.getOffSet(), pagingDefinationDTO.getPageSize(), (Sort.by(new Sort.Order(null, pagingDefinationDTO.getSortBY()))));
        Principal principal = request.getUserPrincipal();
        String email = principal.getName();
        Customer customer = customerRepository.findCustomerByemailId(email);
        if (customer.getOrders().size() == 0)
            throw new NotFoundException("Order Not Found");
        List<ViewOrderDTO> viewOrderDTOList = new ArrayList<>();
        for (Order order : customer.getOrders()) {
            List<ViewOrderProductDTO> orderProductList = new ArrayList<>();
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                ViewOrderProductDTO viewOrderProductDTO = new ViewOrderProductDTO();
                mm.map(orderProduct, viewOrderProductDTO);
                viewOrderProductDTO.setMetadata(orderProduct.getMetaData().toMap());
                orderProductList.add(viewOrderProductDTO);
            }
            ViewOrderDTO viewOrderDTO = new ViewOrderDTO();
            mm.map(order, viewOrderDTO);
            viewOrderDTO.setOrderProducts(orderProductList);
            viewOrderDTOList.add(viewOrderDTO);
        }
        return new PageImpl<>(viewOrderDTOList, pageable, viewOrderDTOList.size());
    }


    public Page<ViewOrderProductDTO> getSellerAllOrder(PagingDefinationDTO pagingDefinationDTO) {
        Pageable pageable = PageRequest.of(pagingDefinationDTO.getOffSet(), pagingDefinationDTO.getPageSize(), (Sort.by(new Sort.Order(null, pagingDefinationDTO.getSortBY()))));
        String email = loggedInService.getLoggedInUser();
        Seller seller = sellerRepository.findSellerByemailId(email);
        List<ProductVariation> productVariations = new ArrayList<>();
        if (seller.getProducts() == null || seller.getProducts().size() == 0)
            throw new NotFoundException("No order Found");
        for (Product product : seller.getProducts()) {
            if (product.getProductVariations() != null && product.getProductVariations().size() != 0) {
                productVariations.addAll(product.getProductVariations());
            }
        }
        if (productVariations.size() == 0)
            throw new NotFoundException("No Order Exist");
        List<ViewOrderProductDTO> viewOrderProductDTOS = new ArrayList<>();
        for (ProductVariation productVariation : productVariations) {
            List<OrderProduct> orderProducts = orderProductRepository.findByProductVariation(productVariation);
            if (orderProducts.size() > 0) {
                for (OrderProduct orderProduct : orderProducts) {
                    ViewOrderProductDTO viewOrderProductDTO = new ViewOrderProductDTO();
                    mm.map(orderProduct, viewOrderProductDTO);
                    viewOrderProductDTO.setMetadata(orderProduct.getMetaData().toMap());
                    viewOrderProductDTOS.add(viewOrderProductDTO);
                }
            }
        }
        if (viewOrderProductDTOS.size() == 0)
            throw new NotFoundException("NO Order Exist");
        return new PageImpl<>(viewOrderProductDTOS, pageable, viewOrderProductDTOS.size());
    }


    //seller
    public ResponseEntity<SuccessResponse> updateStatus(Long orderProductId, OrderStatusDTO orderStatus) {
        String email = loggedInService.getLoggedInUser();
        Seller seller = sellerRepository.findSellerByemailId(email);
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(orderProductId);
        if (orderProduct.isEmpty() || !orderProduct.get().getProductVariation().getProduct().getSeller().getId().equals(seller.getId()))
            throw new NotFoundException("Order Product Not Found");
        OrderStatus existOrderStatus = orderProduct.get().getOrderStatus();
        existOrderStatus.setToStatus(orderStatus.getToStatus());
        existOrderStatus.setFromStatus(orderStatus.getFromStatus());
        orderProduct.get().setOrderStatus(existOrderStatus);
        try {
            orderProductRepository.save(orderProduct.get());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Order Status Update Successfully"));
    }


    //admin
    public Page<ViewOrderDTO> getAllOrders(PagingDefinationDTO pagingDefinationDTO) {
        Pageable pageable = PageRequest.of(pagingDefinationDTO.getOffSet(), pagingDefinationDTO.getPageSize(), (Sort.by(new Sort.Order(null, pagingDefinationDTO.getSortBY()))));
        Page<Order> orders = orderRepository.findAll(pageable);
        List<ViewOrderDTO> viewOrderDTOList = new ArrayList<>();
        for (Order order : orders) {
            List<ViewOrderProductDTO> orderProductList = new ArrayList<>();
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                ViewOrderProductDTO viewOrderProductDTO = new ViewOrderProductDTO();
                mm.map(orderProduct, viewOrderProductDTO);
                viewOrderProductDTO.setMetadata(orderProduct.getMetaData().toMap());
                orderProductList.add(viewOrderProductDTO);
            }
            ViewOrderDTO viewOrderDTO = new ViewOrderDTO();
            mm.map(order, viewOrderDTO);
            viewOrderDTO.setOrderProducts(orderProductList);
            viewOrderDTOList.add(viewOrderDTO);
        }
        return new PageImpl<>(viewOrderDTOList, pageable, viewOrderDTOList.size());
    }

    //admin
    public ResponseEntity<SuccessResponse> changeStatus(Long orderProductId, OrderStatusDTO orderStatus) {
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(orderProductId);
        if (orderProduct.isEmpty())
            throw new NotFoundException("Order Product Not Found");
        OrderStatus existOrderStatus = orderProduct.get().getOrderStatus();
        existOrderStatus.setToStatus(orderStatus.getToStatus());
        existOrderStatus.setFromStatus(orderStatus.getFromStatus());
        orderProduct.get().setOrderStatus(existOrderStatus);
        try {
            orderProductRepository.save(orderProduct.get());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Order Status Update Successfully"));
    }


    public void productIsvalid(ProductVariation productVariation) {
        if (productVariation == null || !productVariation.getIsActive()
                || productVariation.getProduct().getIsDelete()
                || !productVariation.getProduct().getIsActive())
            throw new NotFoundException("Product Variation Not Found");
    }
    @NotNull
    private Order createOrder(CartOrderDTO cartOrderDTO, Address address, Long totalamount) {
        Order order = new Order();
        order.setAmountPaid(totalamount);
        order.setPaymentMethod(cartOrderDTO.getPaymentMethod());
        order.setOrderCreated(new Date());
        order.setHouseNumber(address.getHouseNumber());
        order.setArea(address.getArea());
        order.setLandmark(address.getLandmark());
        order.setCity(address.getCity());
        order.setState(address.getState());
        order.setPinCode(address.getPinCode());
        order.setCountry(address.getCountry());
        order.setAddressType(address.getAddressType());
        return order;
    }

    @NotNull
    private OrderProduct CreateOrderProduct(String notes, Long quantity, ProductVariation productVariation) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setQuantity(quantity);
        orderProduct.setPrice(quantity * productVariation.getPrice());
        orderProduct.setMetaData(productVariation.getMetadata());
        orderProduct.setProductVariation(productVariation);
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setFromStatus(Status.ORDER_PLACED);
        orderStatus.setToStatus(Status.ORDER_CONFIRMED);
        orderStatus.setNotes(notes);
        orderProduct.setOrderStatus(orderStatus);
        return orderProduct;
    }

}


