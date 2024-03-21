package com.coremodule.coremodule.entities.categories.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class CategoryViewDTO implements Serializable {
    private Long id;
    private String categoryName;
    private Map<Object, Object> fieldListwithvalue = new HashMap();

    private ParentCategoryDTO parentHierarchy=new ParentCategoryDTO();



  private List<ChildCategoryDTO>childCategory=new ArrayList<>();
  //private List<Category>childCategory;


    public CategoryViewDTO(Long id, String categoryName, ParentCategoryDTO parentCategory, List<ChildCategoryDTO> childCategory,Map<Object, Object> metafieldwithvalues) {
        this.parentHierarchy = parentCategory;
        this.id = id;
        this.categoryName = categoryName;
        this.fieldListwithvalue  = metafieldwithvalues;
        this.childCategory = childCategory;
      //  System.out.println(metafieldwithvalues.toString());
    }

    public CategoryViewDTO(Long id, String categoryName, ParentCategoryDTO parentCategory, List<ChildCategoryDTO> childCategoryDTOList) {
        this.id = id;
        this.categoryName = categoryName;
        this.parentHierarchy= parentCategory;
        this.childCategory = childCategoryDTOList;
    }

}
