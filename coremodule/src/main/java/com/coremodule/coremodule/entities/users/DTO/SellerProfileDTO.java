package com.coremodule.coremodule.entities.users.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@NoArgsConstructor

@Data
public class SellerProfileDTO {

    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Boolean isActive;
    private String Contact;
    private String companyName;
    private String gst;

    private String houseNumber;
    private String area;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private Long pinCode;

    public SellerProfileDTO(Long id, String firstName, String middleName, String lastName, Boolean isActive, String contact, String companyName, String gst, String houseNumber, String area, String landmark, String city, String state, String country, Long pinCode) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.isActive = isActive;
        Contact = contact;
        this.companyName = companyName;
        this.gst = gst;
        this.houseNumber = houseNumber;
        this.area = area;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
    }
}
