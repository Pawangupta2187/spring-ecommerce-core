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
public class CustomerProfileDTO {
    private String emailId;
    private String firstName;
    private String middleName;
    private String lastName;
    private Boolean isActive;
    private String Contact;
  //  private String image;


    public CustomerProfileDTO(String emailId, String firstName, String middleName, String lastName, Boolean isActive, String Contact) {
        this.emailId = emailId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.Contact = Contact;
    }
}
