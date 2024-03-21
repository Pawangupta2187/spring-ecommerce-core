package com.coremodule.coremodule.entities.users;

import lombok.Data;

@Data
public class PagingDefinationDTO {
//    max
//            offset
//    sort
//            order
//    query
    private Integer pageSize=5;
    private Integer offSet=0;
    private String orderBY="id";
    private String sortBY="id";
}
