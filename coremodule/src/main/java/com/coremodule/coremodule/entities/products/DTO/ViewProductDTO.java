package com.coremodule.coremodule.entities.products.DTO;

import lombok.Data;

import java.util.HashMap;

@Data
public class ViewProductDTO {

    private String name;
    private String description;
    private String brand;
    private Boolean isActive;
    private Boolean isCancellable;
    private Boolean isReturnable;
    private Boolean isDelete;
    private HashMap<Object,Object>categoryDetails;

}
