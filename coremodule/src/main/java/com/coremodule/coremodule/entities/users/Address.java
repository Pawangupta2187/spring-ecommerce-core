package com.coremodule.coremodule.entities.users;

//import com.coremodule.coremodule.Auditing.Auditable;

import com.coremodule.coremodule.Auditing.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Address extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
       private Long id;
    @NotEmpty(message = "House Number Can't Empty")
    private String houseNumber;
    @NotEmpty(message = "Area Can't Empty")
    private String area;

    private String landmark;
    @NotEmpty(message = "city  Can't Empty")
    private String city;
    @NotEmpty(message = "State Can't Empty")
    private String state;
    @NotEmpty(message = "Country Can't Empty")
    private String country;
    @NotNull
    private Long pinCode;
    @NotEmpty(message = "AddressType Can't Empty")
    private String addressType;

    private Boolean isDelete=false;
    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name="seller_id")
    private Seller seller;


}
