package com.coremodule.coremodule.service;

import com.coremodule.coremodule.entities.categories.Category;
import com.coremodule.coremodule.entities.categories.CategoryMetaDataField;
import com.coremodule.coremodule.entities.categories.CategoryMetaDataFieldValues;
import com.coremodule.coremodule.entities.categories.DTO.*;
import com.coremodule.coremodule.entities.categories.SubCategoryValueKey;
import com.coremodule.coremodule.entities.products.Product;
import com.coremodule.coremodule.entities.products.ProductVariation;
import com.coremodule.coremodule.entities.users.PagingDefinationDTO;
import com.coremodule.coremodule.exception.BadRequestException;
import com.coremodule.coremodule.exception.ConflictException;
import com.coremodule.coremodule.exception.NotFoundException;
import com.coremodule.coremodule.exception.SuccessResponse;
import com.coremodule.coremodule.repository.CategoryMetaDataFieldRepository;
import com.coremodule.coremodule.repository.CategoryMetaDataFieldValuesRepository;
import com.coremodule.coremodule.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {


    @Autowired
    CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMetaDataFieldValuesRepository categoryMetaDataFieldValuesRepository;

    @Autowired
    ModelMapper mm;

    public ResponseEntity<SuccessResponse> addMetaField(CategoryMetaDataField categoryMetaDataField) {
        List<CategoryMetaDataField> categoryMetaDataFieldList = categoryMetaDataFieldRepository.findByMetaNameIgnoreCase(categoryMetaDataField.getMetaName());
        if (categoryMetaDataFieldList.size() > 0)
            throw new ConflictException("Meta Field is Already Exist");
        try {
            categoryMetaDataFieldRepository.save(categoryMetaDataField);
        }catch (Exception ex) {
           throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Meta Field Saved SuccessFully"));
    }

    public Page<CategoryMetaDataFieldDTO> getAllMetaField(PagingDefinationDTO pagingDefinationDTO) {
        Pageable pageable = PageRequest.of(pagingDefinationDTO.getOffSet(), pagingDefinationDTO.getPageSize(), (Sort.by(new Sort.Order(null, pagingDefinationDTO.getSortBY()))));
        Page<CategoryMetaDataFieldDTO> metaDataFieldList = categoryMetaDataFieldRepository.findAllMetaField(pageable);
        if(metaDataFieldList.isEmpty())
            throw new NotFoundException("No MetaField Found");
        return metaDataFieldList;
    }

    public Boolean isCategoryExistTillRoot(Category category, String categoryName) {
        if (category == null)
            return false;
        if (category.getCategoryName().equals(categoryName)) {
            return true;
        } else
            return isCategoryExistTillRoot(category.getParentCategory(), categoryName);

    }


    public ResponseEntity<AddCategoryResponseDTO> addCategory(Category category, Long parentId) {
       System.out.println("called1");
        if (parentId == null || parentId < 0) {
            List<Category> existCategory = categoryRepository.findByCategoryName(category.getCategoryName());
            if(existCategory.size()!=0)
                return ResponseEntity.status(HttpStatus.OK).body(new AddCategoryResponseDTO("Already Exist", existCategory.get(0).getId()));
            try {
                categoryRepository.save(category);
            }catch (Exception ex)
            {
                ex.printStackTrace();
                throw new BadRequestException(ex.getMessage());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new AddCategoryResponseDTO("Category Successfully Save", null));
        } else {
            System.out.println("called2");

            Optional<Category> parentCategory = categoryRepository.findById(parentId);
            if(parentCategory.isEmpty())
                throw new NotFoundException("parent id is not found");
            List<Category>siblingCategory;
            siblingCategory=categoryRepository.findSiblingCategory(category.getCategoryName(),parentCategory.get().getId());
            //List<Category> existCategory = categoryRepository.findByCategoryName(category.getCategoryName());
            if (siblingCategory.size()>0 || isCategoryExistTillRoot(parentCategory.get(), category.getCategoryName()) ) {
                return ResponseEntity.status(HttpStatus.OK).body(new AddCategoryResponseDTO("Already Exist", siblingCategory.get(0).getParentCategory().getId()));
            }

                Category savedCategory;
                try{
                    parentCategory.get().addCategory(category);
                savedCategory = categoryRepository.save(parentCategory.get());
                    System.out.println("called3");

                }catch (Exception ex) {
               throw  new RuntimeException(ex.getMessage());
            }
             return ResponseEntity.status(HttpStatus.CREATED).body(new AddCategoryResponseDTO("Category Saved Successfully", savedCategory.getId()));

        }


    }

    //{"RAM":"2GB"}
    public Map<Object, Object> MappedMetaFieldAndValuesToMap(List<CategoryMetaDataFieldValues> categoryMetaDataFieldValues) {
        Map<Object, Object> fieldListwithvalue = new HashMap<>();
        for (CategoryMetaDataFieldValues categoryMetaDataFieldValue : categoryMetaDataFieldValues) {
            fieldListwithvalue.put(categoryMetaDataFieldValue.getCategoryMetaDataField().getMetaName(), categoryMetaDataFieldValue.getValue());
        }
        return fieldListwithvalue;
    }

    public CategoryViewDTO getCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty())
            throw new NotFoundException("id is not valid");
        List<Category> childCategoryList = categoryRepository.findByParentCategory(category.get());
        List<ChildCategoryDTO> childCategoryDTOS = new ArrayList<>();
        for (Category child : childCategoryList) {
            ChildCategoryDTO childCategoryDTO = new ChildCategoryDTO();
            mm.map(child, childCategoryDTO);
            childCategoryDTOS.add(childCategoryDTO);
        }
        //list of values
        List<CategoryMetaDataFieldValues> categoryMetaDataFieldValues = categoryMetaDataFieldValuesRepository.findByCategory(category.get());
        Map<Object, Object> fieldListwithvalue = MappedMetaFieldAndValuesToMap(categoryMetaDataFieldValues);
//        for (CategoryMetaDataFieldValues categoryMetaDataFieldValue : categoryMetaDataFieldValues) {
//            fieldListwithvalue.put(categoryMetaDataFieldValue.getCategoryMetaDataField().getMetaName(), categoryMetaDataFieldValue.getValue());
//        }
        return new CategoryViewDTO(category.get().getId(), category.get().getCategoryName(), findchain(category.get(), null), childCategoryDTOS, fieldListwithvalue);
    }


    public Page<CategoryViewDTO> getAllCategory(PagingDefinationDTO pagingDefinationDTO){
        Pageable pageable = PageRequest.of(pagingDefinationDTO.getOffSet(), pagingDefinationDTO.getPageSize(), (Sort.by(new Sort.Order(null, pagingDefinationDTO.getSortBY()))));
        Page<Category> categoriesPage = categoryRepository.findAll(pageable);
        //Page<Category> categories = categoriesPage.getContent();
        List<CategoryViewDTO> categoryViewDTOList = new ArrayList<>();
        if (categoriesPage.isEmpty())
            return null;
        for (Category cat : categoriesPage) {
            List<Category> childCategoryList = categoryRepository.findByParentCategory(cat);
            List<ChildCategoryDTO> childCategoryDTOS = new ArrayList<>();
            for (Category child : childCategoryList) {
                ChildCategoryDTO childCategoryDTO = new ChildCategoryDTO();
                mm.map(child, childCategoryDTO);
                childCategoryDTOS.add(childCategoryDTO);
            }
            List<CategoryMetaDataFieldValues> categoryMetaDataFieldValues = categoryMetaDataFieldValuesRepository.findByCategory(cat);
            Map<Object, Object> fieldListwithvalue = MappedMetaFieldAndValuesToMap(categoryMetaDataFieldValues);
//            for (CategoryMetaDataFieldValues categoryMetaDataFieldValue : categoryMetaDataFieldValues) {
//                fieldListwithvalue.put(categoryMetaDataFieldValue.getCategoryMetaDataField().getMetaName(), categoryMetaDataFieldValue.getValue());
//            }
            CategoryViewDTO categoryViewDTO = new CategoryViewDTO(cat.getId(), cat.getCategoryName(), findchain(cat, null), childCategoryDTOS, fieldListwithvalue);
            categoryViewDTOList.add(categoryViewDTO);
        }
        return new PageImpl<>(categoryViewDTOList, pageable, categoryViewDTOList.size());
    }

    public ResponseEntity<SuccessResponse> UpdateCategory(Long id, Category category) {
        Optional<Category> existCategory = categoryRepository.findById(id);//need to check only same level
        if (existCategory.isEmpty())
            throw new NotFoundException("Id not valid");
      //  System.out.println(category.getParentCategory().getId()+"<<");
        List<Category>siblingCategory=new ArrayList<>();
        if(existCategory.get().getParentCategory()!=null) {
            System.out.println(existCategory.get().getParentCategory().getId()+">>");
            siblingCategory = categoryRepository.findSiblingCategory(category.getCategoryName(), existCategory.get().getParentCategory().getId());
        }        else
        {
            System.out.println("no parent");
            siblingCategory= categoryRepository.findDuplicateRoot(category.getCategoryName());
        }


        if (siblingCategory.size() > 0 || isCategoryExistTillRoot(existCategory.get(),category.getCategoryName()))
            throw new ConflictException("Category Name already Exist");
        existCategory.get().setCategoryName(category.getCategoryName());
        categoryRepository.save(existCategory.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Category updated successfully"));
    }

    public ResponseEntity<SuccessResponse> AddMetaDataField(AddCategoryMetaDataFieldValuesDTO addCategoryMetaDataFieldValuesDTO) {

        Long id = addCategoryMetaDataFieldValuesDTO.getCatId();
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty())
            throw new NotFoundException("Category id is not valid");
        Optional<CategoryMetaDataField> categoryMetaDataField = categoryMetaDataFieldRepository.findById(addCategoryMetaDataFieldValuesDTO.getMetaDataId());

        if (categoryMetaDataField.isEmpty())
            throw new NotFoundException("CategoryMetaDataField id is not valid");

        if (addCategoryMetaDataFieldValuesDTO.getValues().length == 0)
            throw new BadRequestException("Atleast one value must exist");

        List<CategoryMetaDataFieldValues> categoryMetaDataFieldValues = categoryMetaDataFieldValuesRepository.findByCategoryAndCategoryMetaDataField(category.get(), categoryMetaDataField.get());
        if (categoryMetaDataFieldValues.size() > 0)
            throw new ConflictException("category and field already associated go for update fields");
        String str = String.join(",", addCategoryMetaDataFieldValuesDTO.getValues());
        categoryMetaDataFieldValuesRepository
                .save(new CategoryMetaDataFieldValues
                        (new SubCategoryValueKey(category.get().getId(), categoryMetaDataField.get().getId()),
                                categoryMetaDataField.get(),
                                category.get(),
                                str
                        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("category and field is associated with values"));

    }


    public ResponseEntity<SuccessResponse> updateMetaDataFieldValues(AddCategoryMetaDataFieldValuesDTO addCategoryMetaDataFieldValuesDTO) {
        Long id = addCategoryMetaDataFieldValuesDTO.getCatId();
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty())
            throw new NotFoundException("Category id is not valid");
        Optional<CategoryMetaDataField> categoryMetaDataField = categoryMetaDataFieldRepository.findById(addCategoryMetaDataFieldValuesDTO.getMetaDataId());
        if (categoryMetaDataField.isEmpty())
            throw new NotFoundException("CategoryMetaDataField id is not valid");
        if (addCategoryMetaDataFieldValuesDTO.getValues().length == 0)
            throw  new BadRequestException("Atleast one value must exist");
        List<CategoryMetaDataFieldValues> categoryMetaDataFieldValues = categoryMetaDataFieldValuesRepository.findByCategoryAndCategoryMetaDataField(category.get(), categoryMetaDataField.get());
        if (categoryMetaDataFieldValues.size() == 0)
            throw new BadRequestException("category and field not associated go for create fields first");

        List<String> existedMetaValues = new ArrayList<>();
        Collections.addAll(existedMetaValues, categoryMetaDataFieldValues.get(0).getValue().split(","));
        List<String> newMetaValues = new ArrayList<>();
        Collections.addAll(newMetaValues, addCategoryMetaDataFieldValuesDTO.getValues());
        for (String newMeta : newMetaValues) {
            if (!existedMetaValues.contains(newMeta)) {
                existedMetaValues.add(newMeta);
            }
        }
        categoryMetaDataFieldValues.get(0).setValue(String.join(",", existedMetaValues));
        categoryMetaDataFieldValuesRepository.save(categoryMetaDataFieldValues.get(0));
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("meta values uppdated Successfully"));
    }


    public ParentCategoryDTO findchain(Category category, ParentCategoryDTO parentCategoryDTO1) {
        if (category.getParentCategory() != null) {
            ParentCategoryDTO parentCategoryDTO2 = new ParentCategoryDTO(category.getId(), category.getCategoryName(), parentCategoryDTO1);
            return findchain(category.getParentCategory(), parentCategoryDTO2);
        }
        return new ParentCategoryDTO(category.getId(), category.getCategoryName(), parentCategoryDTO1);
    }

    public List<CategoryViewDTO> getAllCategoryleaf() {
        List<Category> categories = categoryRepository.findAllLeaf();
        //   System.out.println(category.size()+"xxx");
        List<CategoryViewDTO> categoryViewDTOList = new ArrayList<>();
        for (Category cat : categories) {
            List<CategoryMetaDataFieldValues> categoryMetaDataFieldValues = categoryMetaDataFieldValuesRepository.findByCategory(cat);
            Map<Object, Object> fieldListwithvalue = MappedMetaFieldAndValuesToMap(categoryMetaDataFieldValues);
//          for (CategoryMetaDataFieldValues categoryMetaDataFieldValue : categoryMetaDataFieldValues) {
//                fieldListwithvalue.put(categoryMetaDataFieldValue.getCategoryMetaDataField().getMetaName(), categoryMetaDataFieldValue.getValue());
//          }
            CategoryViewDTO categoryViewDTO = new CategoryViewDTO(cat.getId(), cat.getCategoryName(), findchain(cat, null), null, fieldListwithvalue);
            categoryViewDTOList.add(categoryViewDTO);
        }
        return categoryViewDTOList;
    }

    public List<ChildCategoryDTO> getAllCategoryForCustomer(Long catId) {
        if (catId < 0)//if null
        {
            List<Category> samelevelCategory = categoryRepository.findOnlyParents();
            List<ChildCategoryDTO> childCategoryDTOList = new ArrayList<>();
            for (Category cat : samelevelCategory) {
                ChildCategoryDTO childCategoryDTO = new ChildCategoryDTO();
                mm.map(cat, childCategoryDTO);
                childCategoryDTOList.add(childCategoryDTO);
            }
            return childCategoryDTOList;
        } else {
            Optional<Category> category = categoryRepository.findById(catId);
            if (category.isEmpty())
                throw new NotFoundException("Category id is not valid");
            List<Category> samelevelCategory = categoryRepository.findOnlyChilds(catId);
            List<ChildCategoryDTO> childCategoryDTOList = new ArrayList<>();
            for (Category cat : samelevelCategory) {
                ChildCategoryDTO childCategoryDTO = new ChildCategoryDTO();
                mm.map(cat, childCategoryDTO);
                childCategoryDTOList.add(childCategoryDTO);
            }
            return childCategoryDTOList;
        }

    }

    public FilteringFieldDTO fetchFilteringDetails(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty())
            throw new NotFoundException("Invalid id");
        List<String>brand=category.get().getProducts().stream().map(Product::getBrand).collect(Collectors.toList());
        Long maxprice=category.get().getProducts().stream()
                .map(product ->
                        product.getProductVariations().stream().
                        map(ProductVariation::getPrice).reduce(
                                Long::max).get()
                ).reduce(Long::max).get();
        Long minprice=category.get().getProducts().stream()
                .map(product ->
                        product.getProductVariations().stream().
                                map(ProductVariation::getPrice).reduce(
                                Long::min).get()
                ).reduce(Long::min).get();


        List<CategoryMetaDataFieldValues> categoryMetaDataFieldValues = categoryMetaDataFieldValuesRepository.findByCategory(category.get());
        Map<Object, Object> fieldListwithvalue = MappedMetaFieldAndValuesToMap(categoryMetaDataFieldValues);
//        for (CategoryMetaDataFieldValues categoryMetaDataFieldValue : categoryMetaDataFieldValues) {
//            fieldListwithvalue.put(categoryMetaDataFieldValue.getCategoryMetaDataField().getMetaName(), categoryMetaDataFieldValue.getValue());
//        }
        FilteringFieldDTO filteringFieldDTO = new FilteringFieldDTO(fieldListwithvalue);
        filteringFieldDTO.setBrandList(brand);
        filteringFieldDTO.setMaxPrice(maxprice);
        filteringFieldDTO.setMinPrice(minprice);
        return filteringFieldDTO;
    }

}
