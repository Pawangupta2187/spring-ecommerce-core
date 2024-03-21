package com.coremodule.coremodule.entities.orders;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ViewOrderDTO {

    private Long id;
    private Long amountPaid;
    private Date orderCreated;
    private String paymentMethod;

    //address

    private String houseNumber;
    private String area;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private Long pinCode;
    private String addressType;

    private List<ViewOrderProductDTO> orderProducts=new ArrayList<>();

}
