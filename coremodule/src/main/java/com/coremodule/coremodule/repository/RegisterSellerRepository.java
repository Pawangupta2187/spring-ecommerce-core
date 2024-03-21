package com.coremodule.coremodule.repository;

import com.coremodule.coremodule.entities.users.DTO.SellerProfileDTO;
import com.coremodule.coremodule.entities.users.DTO.sellerdto;
import com.coremodule.coremodule.entities.users.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegisterSellerRepository extends PagingAndSortingRepository<Seller,Long> {

    @Query("select new com.coremodule.coremodule.entities.users.DTO.sellerdto(s.id,s.firstName," +
            "s.middleName, s.lastName,s.emailId,s.isActive,s.companyName," +
            "s.address.houseNumber,s.address.area,s.address.landmark,s.address.city" +
            ",s.address.state,s.address.country,s.address.pinCode,s.address.addressType) from Seller s")
    Page<sellerdto> findAllSellerPartialData(Pageable pageable);

    Seller findSellerByEmailId(String email);
    @Query("from Seller")
    List<Seller> findallseller(Pageable pageable);

      @Query("select new com.coremodule.coremodule.entities.users.DTO.SellerProfileDTO(" +
            "s.id,s.firstName,s.middleName, s.lastName,s.isActive,s.companyContact,s.companyName," +
            "s.gst,s.address.houseNumber,s.address.area,s.address.landmark,s.address.city," +
            "s.address.state,s.address.country,s.address.pinCode) from Seller s where s.emailId=:emailId")
    SellerProfileDTO getSellerProfileByEmailId(@Param("emailId") String emailId);

    Seller findSellerByemailId(String emailId);
//
    List<Seller>findSellerByCompanyNameIgnoreCase(String companyName);
    List<Seller>findSellerByGstIgnoreCase(String gst);

}
