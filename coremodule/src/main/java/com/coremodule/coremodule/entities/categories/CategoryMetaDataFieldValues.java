package com.coremodule.coremodule.entities.categories;

//import com.coremodule.coremodule.Auditing.Auditable;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity
@Data
@NoArgsConstructor
public class CategoryMetaDataFieldValues  {
    @EmbeddedId
    SubCategoryValueKey id;
    @ManyToOne
    @MapsId("catMetaId")
    @JoinColumn(name="category_metaData_field_id")
    private CategoryMetaDataField categoryMetaDataField;

    @ManyToOne
    @MapsId("catId")
    @JoinColumn(name="category_id")
    private Category category;

    @NotEmpty(message = "values Can't be Empty")
    private String value;

    public CategoryMetaDataFieldValues(SubCategoryValueKey id, CategoryMetaDataField categoryMetaDataField, Category category, String value) {
        this.id = id;
        this.categoryMetaDataField = categoryMetaDataField;
        this.category = category;
        this.value = value;
    }

    //    public CategoryMetaDataFieldValues(CategoryMetaDataField categoryMetaDataField, SubCategory subCategory, String value) {

}
