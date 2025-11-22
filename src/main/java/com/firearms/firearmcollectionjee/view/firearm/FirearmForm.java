package com.firearms.firearmcollectionjee.view.firearm;

import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.service.FirearmService;
import com.firearms.firearmcollectionjee.service.WeaponFamilyService;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Named("firearmForm")
@ViewScoped
public class FirearmForm implements Serializable {

    @Inject
    FirearmService firearmService;

    @Inject
    WeaponFamilyService weaponFamilyService;

    private UUID id;
    private String name;
    private Double caliber;
    private Integer magazineCapacity;
    private LocalDate productionDate;
    private WeaponFamily weaponFamily;

    // for binding via f:viewParam when creating with preselected family
    private String weaponFamilyIdParam;

    public void init() throws IOException {
        // if id is set -> edit mode
        if (id != null) {
            Optional<Firearm> f = firearmService.findById(id);
            if (f.isPresent()) {
                Firearm fa = f.get();
                this.name = fa.getName();
                this.caliber = fa.getCaliber();
                this.magazineCapacity = fa.getMagazineCapacity();
                this.productionDate = fa.getProductionDate();
                this.weaponFamily = fa.getWeaponFamily();
            } else {
                FacesContext.getCurrentInstance().getExternalContext()
                        .responseSendError(404, "Firearm not found");
            }
        } else if (weaponFamilyIdParam != null && !weaponFamilyIdParam.isBlank()) {
            try {
                UUID wfId = UUID.fromString(weaponFamilyIdParam);
                this.weaponFamily = weaponFamilyService.findById(wfId).orElse(null);
            } catch (IllegalArgumentException ex) {
                // ignore
            }
        }
    }

    public List<WeaponFamily> getAvailableWeaponFamilies() {
        return weaponFamilyService.findAll();
    }

    public String save() {
        if (id == null) {
            // create
            Firearm f = Firearm.builder()
                    .id(UUID.randomUUID())
                    .name(name)
                    .caliber(caliber == null ? 0.0 : caliber)
                    .magazineCapacity(magazineCapacity == null ? 0 : magazineCapacity)
                    .productionDate(productionDate)
                    .weaponFamily(weaponFamily)
                    .build();
            firearmService.createFirearm(f);
            return "/weaponfamily/weaponfamily_view.xhtml?faces-redirect=true&id=" + (weaponFamily != null ? weaponFamily.getId() : "");
        } else {
            // update
            Optional<Firearm> of = firearmService.findById(id);
            if (of.isPresent()) {
                Firearm f = of.get();
                f.setName(name);
                f.setCaliber(caliber == null ? 0.0 : caliber);
                f.setMagazineCapacity(magazineCapacity == null ? 0 : magazineCapacity);
                f.setProductionDate(productionDate);
                f.setWeaponFamily(weaponFamily);
                firearmService.updateFirearm(f);
                return "/weaponfamily/weaponfamily_view.xhtml?faces-redirect=true&id=" + (weaponFamily != null ? weaponFamily.getId() : "");
            } else {
                return null;
            }
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getCaliber() { return caliber; }
    public void setCaliber(Double caliber) { this.caliber = caliber; }

    public Integer getMagazineCapacity() { return magazineCapacity; }
    public void setMagazineCapacity(Integer magazineCapacity) { this.magazineCapacity = magazineCapacity; }

    public LocalDate getProductionDate() { return productionDate; }
    public void setProductionDate(LocalDate productionDate) { this.productionDate = productionDate; }

    public WeaponFamily getWeaponFamily() { return weaponFamily; }
    public void setWeaponFamily(WeaponFamily weaponFamily) { this.weaponFamily = weaponFamily; }

    public String getWeaponFamilyIdParam() { return weaponFamilyIdParam; }
    public void setWeaponFamilyIdParam(String weaponFamilyIdParam) { this.weaponFamilyIdParam = weaponFamilyIdParam; }
}
