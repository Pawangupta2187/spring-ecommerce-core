package com.coremodule.coremodule.entities.orders;

//import com.coremodule.coremodule.Auditing.Auditable;

import com.coremodule.coremodule.Auditing.Auditable;
import com.coremodule.coremodule.entities.users.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "CustomerOrder")
public class Order extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull(message = "paid amount cant be empty")
    private Long amountPaid;
    @NotNull(message = " date can't be empty")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderCreated;
    @NotEmpty(message = "Payment Method  Can't Empty")
    private String paymentMethod;
     //address
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
    @NotNull(message = "pincode can't be Empty")
    private Long pinCode;
    @NotEmpty(message = "AddressType Can't Empty")
    private String addressType;

    @ManyToOne
    @JoinColumn(name ="customer_id")
    private Customer customer;

    @OneToMany(mappedBy ="order",cascade = CascadeType.ALL)
    private Set<OrderProduct> orderProducts;



    public void addOrderProducts(OrderProduct orderProduct)
    {
        if(orderProduct!=null)
        {
            if(orderProducts==null)
                orderProducts=new HashSet<>();
            orderProducts.add(orderProduct);
            orderProduct.setOrder(this);
        }
    }



}
