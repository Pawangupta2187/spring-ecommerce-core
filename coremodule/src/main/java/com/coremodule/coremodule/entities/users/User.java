package com.coremodule.coremodule.entities.users;

import com.coremodule.coremodule.Auditing.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Set;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor

//@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
//@FieldMatch.List({
//        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
//        @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")
//})
@Inheritance(strategy = InheritanceType.JOINED )
public class User extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    @NotEmpty(message = "email can't be Empty")
    @Email(message = "Email must be correct")
    private String emailId;
   @NotEmpty(message = "First Name has to be present")
     private String firstName;
    private String middleName;
    @NotEmpty(message = "Last Name has to be present")
    private String lastName;


    private String image;
    @NotEmpty(message = "password can't be Empty")
    @Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message="Password must be in format")
    private String password;


    @Transient
    private String confirmPassword;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDelete=false;
    @Column(columnDefinition = "boolean default false")
    private Boolean isActive=false;

    private Integer failedAttempt;

    @Column(columnDefinition = "boolean default true")
    private boolean accountNonLocked=true;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lockTime;



    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name="users_roles",
                joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name="role_id",referencedColumnName = "id"))
    private Set<Role> roles;


//    public void addRoles(Role role)
//    {
//       // role.adduser(this);
//        if(roles==null)
//            roles=new HashSet<>();
//        roles.add(role);
//        //role.setUsers();
//    }
}
