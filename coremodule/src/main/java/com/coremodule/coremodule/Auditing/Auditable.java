package com.coremodule.coremodule.Auditing;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<U>{
    @Column(name = "date_created")
    @CreatedDate
    private Date dateCreated;
    @Column(name = "last_updated")
    @LastModifiedDate
    private Date lastUpdated;
    @CreatedBy
    @Column(name = "created_by")
    private U createdBy;
    @LastModifiedBy
    @Column(name = "last_updated_by")
    private U lastModifiedBy;
}