package com.coremodule.coremodule.entities.users;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private long tokenid;
    private String confirmationToken;
   // private String tokenType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken(User user) {
        final long hoursInMillis = 60L * 60L * 1000L;

        this.user = user;
        expiryDate = new Date(new Date().getTime()+(2L+hoursInMillis));
        confirmationToken = UUID.randomUUID().toString();
    }

    // getters and setters
}