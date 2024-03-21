package com.coremodule.coremodule.entities.users;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@Service
@Data
public class customerdto {
    //select c.id,c.firstName,c.middleName, c.lastName,c.emailId,c.isActive from Customer c"
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String emailId;
    private boolean isActive;

    public customerdto(Long id, String firstName, String middleName, String lastName, String emailId, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.isActive = isActive;
    }
}
