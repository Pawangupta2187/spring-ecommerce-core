package com.coremodule.coremodule.controllers;

import com.coremodule.coremodule.entities.categories.*;
import com.coremodule.coremodule.entities.categories.DTO.*;
import com.coremodule.coremodule.entities.categories.DTO.AddCategoryResponseDTO;
import com.coremodule.coremodule.entities.users.PagingDefinationDTO;
import com.coremodule.coremodule.exception.SuccessResponse;
import com.coremodule.coremodule.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequestMapping("/category")
@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    //api 1
    @PostMapping("/admin/addmetafield")
    public ResponseEntity<SuccessResponse>AddMetaField(@RequestBody @Valid CategoryMetaDataField categoryMetaDataField) {
        return categoryService.addMetaField(categoryMetaDataField);
    }


    //api2
    @GetMapping("/admin/getallmetafield")
    public Page<CategoryMetaDataFieldDTO> getAllMetaField(@RequestBody(required = false) PagingDefinationDTO pagingDefinationDTO) {
        if(pagingDefinationDTO==null)
            pagingDefinationDTO=new PagingDefinationDTO();
       return categoryService.getAllMetaField(pagingDefinationDTO);
    }

    //api3
    @PostMapping("/admin/addcategory")
    public ResponseEntity<AddCategoryResponseDTO>addSubcategory(@Valid @RequestBody Category category, @RequestParam(required = false) Long parentId) {
        return categoryService.addCategory(category, parentId);
    }

    //api 4
    @GetMapping("/admin/getcategory")
    public CategoryViewDTO getCategory(@RequestParam Long catId)
    {
            return categoryService.getCategory(catId);
    }

    //api 5
    @GetMapping("/admin/getallcategory")
    public Page<CategoryViewDTO>getAllCategory(@RequestBody(required = false) PagingDefinationDTO pagingDefinationDTO) {
      if(pagingDefinationDTO==null)
          pagingDefinationDTO=new PagingDefinationDTO();
        return categoryService.getAllCategory(pagingDefinationDTO);
    }

    //updated category api 7
    @PutMapping("/admin/updatecategory/{id}")
    public ResponseEntity<SuccessResponse>updateCategory(@PathVariable("id") Long id,@Valid @RequestBody Category category) {
      System.out.println("--"+id);
        return categoryService.UpdateCategory(id,category);
    }


    //api 8 add
    @PostMapping("/admin/addCategoryMetaFieldValues")
    public ResponseEntity<SuccessResponse>AddMetaDataField(@RequestBody AddCategoryMetaDataFieldValuesDTO addCategoryMetaDataFieldValuesDTO) {
        return categoryService.AddMetaDataField(addCategoryMetaDataFieldValuesDTO);
    }

    //api 9
    @PutMapping("/admin/updatemetadatafieldvalues")
    public  ResponseEntity<SuccessResponse> updateMetaDataFieldValues(@RequestBody AddCategoryMetaDataFieldValuesDTO addCategoryMetaDataFieldValuesDTO) {
        return categoryService.updateMetaDataFieldValues(addCategoryMetaDataFieldValuesDTO);
    }

    //seller api 1
    @GetMapping("/seller/viewcategory")
    public   List<CategoryViewDTO> getAllCategoryleaf()
    {
        return categoryService.getAllCategoryleaf();
    }

    //api for customer category 1
    @GetMapping("/customer/viewcategory")
    public List<ChildCategoryDTO> getAllCategoryForCustomer(@RequestParam Long catId) {
        return categoryService.getAllCategoryForCustomer(catId);

    }

    //api2
    @GetMapping("/customer/filteroption")
    public FilteringFieldDTO fetchFilteringDetails(@RequestParam Long catId) {
        return categoryService.fetchFilteringDetails(catId);
    }
}
