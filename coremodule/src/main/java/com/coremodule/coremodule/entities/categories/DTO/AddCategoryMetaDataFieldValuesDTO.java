package com.coremodule.coremodule.entities.categories.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class AddCategoryMetaDataFieldValuesDTO {
    @NotNull(message = "Please Provide Category Id")
    private Long catId;
    @NotNull(message = "Please Provide metadata Id")
    private Long metaDataId;

    private String[] values;

    public AddCategoryMetaDataFieldValuesDTO(Long catId, Long metaDataId, String[] values) {
        this.catId = catId;
        this.metaDataId = metaDataId;
        this.values = values;
    }
}
