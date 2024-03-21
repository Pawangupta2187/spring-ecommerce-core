package com.coremodule.coremodule.entities.users;

import com.coremodule.coremodule.entities.products.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

//GST
//COMPANY_CONTACT
//COMPANY_NAME

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@PrimaryKeyJoinColumn(name="id")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"gst"})})
public class Seller extends User  {
    @Column(unique = true)

    @NotEmpty(message = "Gst Can't be Empty")
    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",message="gst must be valid")
    private String gst;
    @NotEmpty(message = "Contact Can't be Empty")
    @Size(min=10,max=10,message = "contact must be of 10 digit")
    private String companyContact;
    @NotEmpty(message = "Company Name Can't be Empty")
    @Column(unique = true)
    private String companyName;
    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private Set<Product>products;

    @OneToOne(mappedBy = "seller",cascade = CascadeType.ALL)
    private Address address;

    public void addAddress(Address address)
    {
        this.address=address;
        address.setSeller(this);
    }

}
