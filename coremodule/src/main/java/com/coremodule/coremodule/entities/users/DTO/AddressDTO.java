package com.coremodule.coremodule.entities.users.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Size;

@Service
@Getter
@Setter
@NoArgsConstructor
@Data
public class AddressDTO {

    @Size(min=1,message = "Should be atleast 1 Character")
    private String houseNumber;
    @Size(min=1,message = "Should be atleast 1 Character")
    private String area;
    @Size(min=1,message = "Should be atleast 1 Character")
    private String landmark;
    @Size(min=1,message = "Should be atleast 1 Character")
    private String city;
    @Size(min=1,message = "Should be atleast 1 Character")
    private String state;
    @Size(min=1,message = "Should be atleast 1 Character")
    private String country;
  //  @NotNull(message = "Pincode Can't be Empty")
  //todo:validation here
  private Long pinCode;
 //   @NotEmpty(message = "AddressType Can't Empty")
    private String addressType;

    public AddressDTO(String houseNumber, String area, String landmark, String city, String state, String country, Long pinCode, String addressType) {
        this.houseNumber = houseNumber;
        this.area = area;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
        this.addressType = addressType;
    }
}
