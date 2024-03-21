package com.coremodule.coremodule.entities.categories.DTO;

import lombok.Data;

@Data
public class AddCategoryResponseDTO {
    private String message;
    private Long ParentId;

    public AddCategoryResponseDTO(String message, Long parentId) {
        this.message = message;
        ParentId = parentId;
    }
}
