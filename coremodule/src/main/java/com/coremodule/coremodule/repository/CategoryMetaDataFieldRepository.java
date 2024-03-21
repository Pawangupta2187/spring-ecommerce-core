package com.coremodule.coremodule.repository;

import com.coremodule.coremodule.entities.categories.CategoryMetaDataField;
import com.coremodule.coremodule.entities.categories.DTO.CategoryMetaDataFieldDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryMetaDataFieldRepository extends PagingAndSortingRepository<CategoryMetaDataField,Long> {

    List<CategoryMetaDataField> findByMetaNameIgnoreCase(String metaname);
    @Query("select new com.coremodule.coremodule.entities.categories.DTO.CategoryMetaDataFieldDTO(c.id ,c.metaName) from CategoryMetaDataField c")
    Page<CategoryMetaDataFieldDTO> findAllMetaField(Pageable pageable);

}
