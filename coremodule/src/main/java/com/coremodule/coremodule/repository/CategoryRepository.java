package com.coremodule.coremodule.repository;

import com.coremodule.coremodule.entities.categories.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category,Long> {


    List<Category>findByCategoryName(String categoryName);

    List<Category>findByParentCategory(Category id);

    @Query("from Category c where c.id NOT IN(select p.parentCategory.id from Category p where p.parentCategory.id IS NOT NULL)")
    List<Category>findAllLeaf();

    @Query("from Category C where C.parentCategory.id IS NULL")
    List<Category>findOnlyParents();

    @Query("from Category C where C.parentCategory.id=:catId")
    List<Category>findOnlyChilds(@Param("catId")Long id);

    @Query("from Category C where C.categoryName Like :categoryName And C.parentCategory.id=:parentId")
    List<Category>findSiblingCategory(@Param("categoryName") String categoryName,@Param("parentId")Long parentId);

    @Query("from Category C where C.categoryName Like :categoryName And C.parentCategory.id IS NULL")
    List<Category>findDuplicateRoot(@Param("categoryName") String categoryName);
}
