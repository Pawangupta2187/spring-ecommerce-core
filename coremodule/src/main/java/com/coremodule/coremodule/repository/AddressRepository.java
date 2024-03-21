package com.coremodule.coremodule.repository;


import com.coremodule.coremodule.entities.users.Address;
import com.coremodule.coremodule.entities.users.DTO.AddressDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address,Long> {

    @Query("select new com.coremodule.coremodule.entities.users.DTO.AddressDTO(a.houseNumber,a.area,a.landmark,a.city," +
            "a.state,a.country,a.pinCode,a.addressType)from Address a where a.customer.id=:id AND a.isDelete=false")
    List<AddressDTO> findAddressesByCutomerId(@Param("id")Long id);

    @Query("from Address a where a.id=:addressId AND a.customer.id=:custId ")
 List<Address>findAddressesByCutomerIdANDAddressId(@Param("custId") Long custId,@Param("addressId") Long addressId);

//    @Query("update Address a set a.isDelete=true where a.id=:id")
//    void deleteAddress(@Param("id") Long id);
}
