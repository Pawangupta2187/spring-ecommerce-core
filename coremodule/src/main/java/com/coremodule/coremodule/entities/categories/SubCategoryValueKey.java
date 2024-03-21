package com.coremodule.coremodule.entities.categories;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@NoArgsConstructor
public class SubCategoryValueKey implements Serializable {

    private Long catId;
    private Long catMetaId;

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public SubCategoryValueKey(Long catId, Long catMetaId) {
        this.catId = catId;
        this.catMetaId = catMetaId;
    }

    public Long getCatMetaId() {
        return catMetaId;
    }

    public void setCatMetaId(Long catMetaId) {
        this.catMetaId = catMetaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubCategoryValueKey that = (SubCategoryValueKey) o;
        return Objects.equals(catId, that.catId) && Objects.equals(catMetaId, that.catMetaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catId, catMetaId);
    }
}
