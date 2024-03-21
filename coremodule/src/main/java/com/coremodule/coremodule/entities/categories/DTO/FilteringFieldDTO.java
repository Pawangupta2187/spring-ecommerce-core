package com.coremodule.coremodule.entities.categories.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class FilteringFieldDTO {
    private Map<Object, Object> fieldListwithvalue = new HashMap<>();
    private List<String> brandList;
    private Long maxPrice;
    private Long minPrice;

    public FilteringFieldDTO(Map<Object, Object> fieldListwithvalue) {
        this.fieldListwithvalue = fieldListwithvalue;
    }
}
