package com.coremodule.coremodule.entities.categories.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ParentCategoryDTO implements Serializable {
    private Long id;
    private String categoryName;
    private ParentCategoryDTO child;

    public ParentCategoryDTO(Long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public ParentCategoryDTO(Long id, String categoryName, ParentCategoryDTO parentCategoryDTO) {
        this.id = id;
        this.categoryName = categoryName;
        this.child = parentCategoryDTO;
    }
}
