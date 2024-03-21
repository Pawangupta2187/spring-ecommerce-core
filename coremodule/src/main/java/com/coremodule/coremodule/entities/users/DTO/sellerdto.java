package com.coremodule.coremodule.entities.users.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

//select s.id,s.firstName,s.middleName, s.lastName,s.emailId,s.isActive,s.companyName,s.address
@Service
@Getter
@Setter
@NoArgsConstructor

@Data

public class sellerdto {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String emailId;
    private Boolean isActive;
    private String companyName;


//    private String houseNumber;
//    private String area;
//    private String landmark;
//    private String city;
//    private String state;
//    private String country;
//    private Long pinCode;
//    private String addressType;
    private AddressDTO address;

    public sellerdto(Long id, String firstName, String middleName, String lastName, String emailId, Boolean isActive, String companyName, String houseNumber, String area, String landmark, String city, String state, String country, Long pinCode, String addressType) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.isActive = isActive;
        this.companyName = companyName;
        address=new AddressDTO();
        address.setHouseNumber(houseNumber);
        address.setAddressType(addressType);
        address.setArea(area);
        address.setCity(city);
        address.setLandmark(landmark);
        address.setPinCode(pinCode);
        address.setState(state);
        address.setCountry(country);
//        this.houseNumber = ;
//        this.area = ;
//        this.landmark =;
//        this.city = ;
//        this.state = ;
//        this.country = ;
//        this.pinCode = ;
//        this.addressType = ;
    }


//    public sellerdto(Long id, String firstName, String middleName, String lastName, String emailId, Boolean isActive, String companyName,String houseNumber,
//    String area,String landmark,String city,String state,String country,Long pinCode,String addressType) {
//        this.id = id;
//        this.firstName = firstName;
//        this.middleName = middleName;
//        this.lastName = lastName;
//        this.emailId = emailId;
//        this.isActive = isActive;
//        this.companyName = companyName;
//      //  System.out.println(address.toString());
//        //this.address = address;
////        Address address1=new Address();
////        address1.setHouseNumber();
////        this.houseNumber=houseNumber;
//    }
}
