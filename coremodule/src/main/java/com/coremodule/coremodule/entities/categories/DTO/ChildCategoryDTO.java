package com.coremodule.coremodule.entities.categories.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ChildCategoryDTO implements Serializable {
    private Long id;
    private String categoryName;

    public ChildCategoryDTO(Long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
}
