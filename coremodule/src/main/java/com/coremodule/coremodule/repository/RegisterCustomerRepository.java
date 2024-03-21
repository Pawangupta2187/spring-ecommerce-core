package com.coremodule.coremodule.repository;

import com.coremodule.coremodule.entities.users.*;
import com.coremodule.coremodule.entities.users.DTO.CustomerProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegisterCustomerRepository extends PagingAndSortingRepository<Customer,Long> {

//Id
//Full Name
//Email
//Is_Active

    Customer findCustomerByemailId(String email);

    @Query("select new  com.coremodule.coremodule.entities.users.customerdto(c.id,c.firstName,c.middleName, c.lastName,c.emailId,c.isActive) from Customer c")
    Page<customerdto> findAllCustomerPartialData(Pageable pageable);

    //   private String email;
    //    private String firstName;
    //    private String middleName;
    //    private String lastName;
    //    private String isActive;
    //    private String contact;
    //    private String image;
    @Query("select new com.coremodule.coremodule.entities.users.DTO.CustomerProfileDTO(c.emailId,c.firstName,c.middleName, c.lastName,c.isActive,c.Contact)from Customer c where c.emailId=:emailId")
    CustomerProfileDTO customerProfile(@Param("emailId")String emailId);
// private String houseNumber;
//    private String area;
//    private String landmark;
//    private String city;
//    private String state;
//    private String country;
//    private Long pinCode;
//    private String addressType;
//@Query("select new com.coremodule.coremodule.entities.users.DTO.AddressDTO(s.addresses.houseNumber,s.addresses.area,s.addresses.landmark,s.addresses.city" +
//        ",s.addresses.state,s.addresses.country,s.addresses.pinCode,s.addresses.addressType) from Customer s where s.id=:id")
////    @Query("select new com.coremodule.coremodule.entities.users.DTO.AddressDTO(a.houseNumber,a.area,a.landmark,a.city," +
////            "a.state,a.country,a.pinCode,a.addressType) from address a where a.id=:id  ")
//    List<AddressDTO>getCustomerAddressesById(@Param("id") Long id);
//
//@Query("select s.addresses.houseNumber,s.addresses.area,s.addresses.landmark,s.addresses.city,s.addresses.state,s.addresses.country,s.addresses.pinCode,s.addresses.addressType from Customer s where id=:id")
@Query( "select distinct C from Customer C\n" +
        "join fetch C.addresses ad\n" +
        "order by ad.id")
List<Customer>getCustomerAddressesById(@Param("id") Long id);

//
//    "s.houseNumber,s.area,s.landmark,s.city" +
//            ",s.state,s.country,s.pinCode,s.addressType" +
//            "from Customer s join s.addresses"
}
