package com.coremodule.coremodule.entities.users;

import com.coremodule.coremodule.entities.cart.Cart;
import com.coremodule.coremodule.entities.orders.Order;
import com.coremodule.coremodule.entities.products.ProductReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@PrimaryKeyJoinColumn(name="id")

public class Customer extends User {

    @Size(min=10,max = 10,message = "Contact must be of 10 number")
    private String Contact;
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)//mappedbyuser
    private Set<Order> orders;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private Set<ProductReview>productReviews;

    @OneToMany(mappedBy ="customer",cascade = CascadeType.ALL)
    private Set<Cart>carts;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Address>addresses;

    public void addAddress(Address address)
    {
        if(address!=null)
        {
            if(addresses==null)
                addresses=new HashSet<Address>();
            addresses.add(address);
            address.setCustomer(this);
        }
    }

    public void addCart(Cart cart)
    {
        if(cart!=null)
        {
            if(carts==null)
                carts=new HashSet<Cart>();
            carts.add(cart);
            cart.setCustomer(this);
        }
    }
    public void addOrder(Order order)
    {
        if(order!=null)
        {
            if(orders==null)
                orders=new HashSet<>();
            orders.add(order);
            order.setCustomer(this);
        }
    }

}
