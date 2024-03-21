package com.coremodule.coremodule.entities.categories;

//import com.coremodule.coremodule.Auditing.Auditable;

import com.coremodule.coremodule.Auditing.Auditable;
import com.coremodule.coremodule.entities.products.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Category  extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty(message = "subcategory name must be exist")
    @Size(min=1,message = "Should be atleast 1 Character")
    private String categoryName;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="parent_id")
    private Category parentCategory;

    @OneToMany(mappedBy="parentCategory", cascade = CascadeType.ALL)
    private Set<Category> childCategory = new HashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<CategoryMetaDataFieldValues> categoryMetaDataFieldsValuesSet;


    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private Set<Product> products;

    public void addCategoryMetaDataFieldValues(CategoryMetaDataFieldValues categoryMetaDataFieldValues)
    {
        if(categoryMetaDataFieldValues==null)
        {
            categoryMetaDataFieldsValuesSet=new HashSet<>();
        }
        categoryMetaDataFieldsValuesSet.add(categoryMetaDataFieldValues);

    }
    public void addCategory(Category category)
    {
        if(category!=null)
        {
            childCategory.add(category);
           category.setParentCategory(this);
        }
    }

}
