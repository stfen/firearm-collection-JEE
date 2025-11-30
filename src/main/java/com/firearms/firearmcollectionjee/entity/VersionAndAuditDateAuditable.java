package com.firearms.firearmcollectionjee.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import java.time.LocalDateTime;

/**
 * Base super class providing support for optimistic locking version, creation date, and last modification date.
 */
@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class VersionAndAuditDateAuditable {

    /**
     * Edit version for optimistic locking.
     */
    @Version
    private Long version;

    /**
     * Creation date.
     */
    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    /**
     * Last modification date.
     */
    @Column(name = "last_modification_date_time")
    private LocalDateTime lastModificationDateTime;

    /**
     * Update creation datetime before persisting.
     */
    @PrePersist
    public void updateCreationDateTime() {
        creationDateTime = LocalDateTime.now();
        lastModificationDateTime = LocalDateTime.now();
    }

    /**
     * Update last modification datetime before updating.
     */
    @PreUpdate
    public void updateLastModificationDateTime() {
        lastModificationDateTime = LocalDateTime.now();
    }

}
