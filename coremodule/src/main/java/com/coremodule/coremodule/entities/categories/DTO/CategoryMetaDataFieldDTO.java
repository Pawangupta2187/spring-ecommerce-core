package com.coremodule.coremodule.entities.categories.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class CategoryMetaDataFieldDTO {
    private Long id;
    @NotEmpty(message = "meta name can't empty")
    private String metaName;

    public CategoryMetaDataFieldDTO(Long id, String metaName) {
        this.id = id;
        this.metaName = metaName;
    }
}
