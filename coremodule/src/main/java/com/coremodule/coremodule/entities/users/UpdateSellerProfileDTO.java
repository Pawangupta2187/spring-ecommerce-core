package com.coremodule.coremodule.entities.users;

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
public class UpdateSellerProfileDTO {
   // @NotBlank(message = "can't blank")
   @Size(min=1,message = "Should be atleast 1 Character")
    private String firstName;
    @Size(min=1,message = "Should be atleast 1 Character")
    private String middleName;
    @Size(min=1,message = "Should be atleast 1 Character")
    private String lastName;
    @Size(min=10,max=10,message = "contact must be of 10 digit")
    private String companyContact;

}
