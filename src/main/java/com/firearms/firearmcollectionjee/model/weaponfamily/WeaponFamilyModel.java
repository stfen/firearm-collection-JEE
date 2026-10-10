package com.firearms.firearmcollectionjee.model.weaponfamily;

import com.firearms.firearmcollectionjee.entity.enums.AmmoType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

/**
 * View model for WeaponFamily to be used by JSF views (separates entity from view layer).
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class  WeaponFamilyModel implements Serializable {

	private UUID id;
	private String name;
	private int optimalRange;
	private AmmoType ammoType;

}
