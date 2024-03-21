package com.coremodule.coremodule.entities.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@PrimaryKeyJoinColumn(name="id")
public class Admin extends User {

    private String contactnumber;
}
