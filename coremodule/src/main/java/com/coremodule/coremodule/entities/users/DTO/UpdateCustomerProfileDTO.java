package com.coremodule.coremodule.entities.users.DTO;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UpdateCustomerProfileDTO {

//    private String emailId;
//    @NotEmpty(message = "first Name Can't be Empty")
@Size(min=1,message = "Should be atleast 1 Character")
private String firstName;
  //  @NotEmpty(message = "middle Name Can't be Empty")
  @Size(min=1,message = "Should be atleast 1 Character")
  private String middleName;
   // @NotEmpty(message="last Name Can't be Empty")
   @Size(min=1,message = "Should be atleast 1 Character")
   private String lastName;
    @Size(min=10,max = 10,message = "Contact must be of 10 number")
    private String Contact;

}
