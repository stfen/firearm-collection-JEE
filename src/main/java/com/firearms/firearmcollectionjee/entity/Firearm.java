package com.firearms.firearmcollectionjee.entity;

import com.firearms.firearmcollectionjee.validation.ValidCaliber;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entity class for game characters' professions (classes). Describes name of the profession and skills available on
 * different levels.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "firearms")
public class Firearm extends VersionAndAuditDateAuditable implements Serializable {

    @Id
    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @ValidCaliber(message = "Caliber must be between 1.0 and 40.0 mm")
    @Positive(message = "Caliber must be positive")
    private double caliber;

    @Column(name = "magazine_capacity")
    @Min(value = 1, message = "Magazine capacity must be at least 1")
    private int magazineCapacity;

    @ManyToOne
    @JoinColumn(name = "weapon_family")
    @NotNull(message = "Weapon family is required")
    private WeaponFamily weaponFamily;

    @Column(name = "production_date")
    @PastOrPresent(message = "Production date cannot be in the future")
    private LocalDate productionDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
