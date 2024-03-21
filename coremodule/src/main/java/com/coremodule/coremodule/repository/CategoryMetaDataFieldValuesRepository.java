package com.coremodule.coremodule.repository;

import com.coremodule.coremodule.entities.categories.Category;
import com.coremodule.coremodule.entities.categories.CategoryMetaDataField;
import com.coremodule.coremodule.entities.categories.CategoryMetaDataFieldValues;
import com.coremodule.coremodule.entities.categories.SubCategoryValueKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryMetaDataFieldValuesRepository extends CrudRepository<CategoryMetaDataFieldValues, SubCategoryValueKey> {

//    List<CategoryMetaDataFieldValues> findByEmployeeIdDepartmentId(Long Metaid);
    List<CategoryMetaDataFieldValues>findByCategoryAndCategoryMetaDataField(Category catid, CategoryMetaDataField metaDataField);
    List<CategoryMetaDataFieldValues>findByCategory(Category category);
}
