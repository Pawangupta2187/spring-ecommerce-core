package com.coremodule.coremodule.controllers;

import com.coremodule.coremodule.entities.products.*;
import com.coremodule.coremodule.entities.products.DTO.*;
import com.coremodule.coremodule.entities.users.PagingDefinationDTO;
import com.coremodule.coremodule.exception.SuccessResponse;
import com.coremodule.coremodule.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;


    //Api 1
    @PostMapping("seller/addproduct")
    public ResponseEntity<SuccessResponse> addProduct(HttpServletRequest request, @RequestParam Long categoryId, @RequestBody Product product) {
        Principal principal = request.getUserPrincipal();
        return productService.addProduct(product,categoryId,principal.getName());
    }

    //api 2
    @PostMapping("/seller/addvariation")
    public ResponseEntity<SuccessResponse>addVariation(@RequestParam Long productId,@RequestBody AddProductVariationDTO productVariationDTO) {
       return productService.addVariation(productId,productVariationDTO);
    }

    //api 3
    @GetMapping("/seller/viewproduct")
    public ViewProductDTO viewProduct(HttpServletRequest request, @RequestParam Long productId) {
        Principal principal = request.getUserPrincipal();
        return productService.viewProduct(productId,principal.getName());
    }
    //api 4
    @GetMapping("/seller/viewproductvariation")
    public ViewVariationDTO viewProductVariation(@RequestParam Long variationId, HttpServletRequest request) {
            Principal principal = request.getUserPrincipal();
            return  productService.viewProductVariation(variationId,principal.getName());
    }
    //api 5
    @GetMapping("/seller/viewallproduct")
    public Page<ViewProductDTO> getALlSellerProduct(HttpServletRequest request, @RequestBody(required = false) PagingDefinationDTO pagingDefinationDTO) {
        if(pagingDefinationDTO==null)
            pagingDefinationDTO=new PagingDefinationDTO();
        Principal principal = request.getUserPrincipal();
        return  productService.getALlSellerProduct(principal.getName(),pagingDefinationDTO);
    }

    //api 6
    @GetMapping("seller/viewproductallvariation")
    public Page<ViewVariationDTO>viewProductAllVariation(@RequestBody(required = false) PagingDefinationDTO pagingDefinationDTO,@RequestParam Long productId,HttpServletRequest request) {
        if(pagingDefinationDTO==null)
            pagingDefinationDTO=new PagingDefinationDTO();
        Principal principal = request.getUserPrincipal();
        return  productService.viewProductAllVariation(productId,principal.getName(),pagingDefinationDTO);

    }
    //api 7
    @DeleteMapping("seller/deleteproduct/{id}")
    public ResponseEntity<SuccessResponse>deleteProduct(@PathVariable("id") Long productId,HttpServletRequest request) {
            Principal principal = request.getUserPrincipal();
            return  productService.deleteProduct(productId,principal.getName());
    }
//api 8
    @PutMapping("/seller/updateproduct/{id}")
    public ResponseEntity<SuccessResponse>updateProduct(@RequestBody UpdateProductDTO productDTO, @PathVariable("id") Long productId, HttpServletRequest request) {
            Principal principal = request.getUserPrincipal();
            return productService.updateProduct(productDTO,productId,principal.getName());
    }

    //api 9
    @PutMapping("/seller/updatevariation/{id}")
    public ResponseEntity<SuccessResponse>updateVariation(@PathVariable("id") Long variationId,@RequestBody AddProductVariationDTO productVariationDTO,HttpServletRequest request) {
            Principal principal = request.getUserPrincipal();
            return productService.updateVariation(productVariationDTO,variationId,principal.getName());
    }

    //Customer ap1
    @GetMapping("/customer/viewproduct")
    public CustomerViewProductDTO customerViewProduct(@RequestParam Long productId, HttpServletRequest request) {
            return productService.customerViewProduct(productId);
    }
    //api2
    @GetMapping("/customer/viewcategoryallproduct")
    public Page<CustomerViewProductDTO>customerViewAllProduct(@RequestParam Long categoryId,HttpServletRequest request) {
       return productService.customerViewAllProduct(categoryId);

    }


    //api 3
    @GetMapping("/customer/similarproduct")
    public Page<CustomerViewProductDTO>customerAllsimilarProduct(@RequestParam Long productId,@RequestBody(required = false) PagingDefinationDTO pagingDefination) {
        return productService.customerAllsimilarProduct(productId,pagingDefination);
    }

    //admin api1
    @GetMapping("/admin/viewproduct")
    public AdminViewProductDTO adminViewProductDTO(@RequestParam Long productId) {
        return productService.adminViewProduct(productId);
    }

    //api 2
    @GetMapping("/admin/viewallproducts")
    public Page<AdminViewProductDTO>adminViewAllProduct(@RequestBody(required = false) PagingDefinationDTO pagingDefination) {
        if(pagingDefination==null)
            pagingDefination=new PagingDefinationDTO();
        return productService.adminViewAllProduct(pagingDefination);
    }

    //api 3
    @PutMapping("/admin/deactivateproduct/{id}")
    public ResponseEntity<SuccessResponse>deActivateProduct(@PathVariable("id") Long productId){
        return productService.deActivateProduct(productId);
    }
    //api4
    @PutMapping("/admin/activateproduct/{id}")
    public ResponseEntity<SuccessResponse>activateProduct(@PathVariable("id") Long productId){
        return productService.activateProduct(productId);
    }
}
