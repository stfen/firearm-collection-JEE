package com.firearms.firearmcollectionjee.view.firearm;

import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.service.FirearmService;
import com.firearms.firearmcollectionjee.service.WeaponFamilyService;
import jakarta.ejb.EJBException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

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

    @Inject
    jakarta.servlet.http.HttpServletRequest request;

    private UUID id;
    private Long version;
    private String name;
    private Double caliber;
    private Integer magazineCapacity;
    private LocalDate productionDate;
    private WeaponFamily weaponFamily;

    // for binding via f:viewParam when creating with preselected family
    private String weaponFamilyIdParam;

    // for conflict resolution
    private boolean conflictDetected = false;
    private Firearm currentDbFirearm;
    private String userAttemptedName;
    private Double userAttemptedCaliber;
    private Integer userAttemptedMagazineCapacity;
    private LocalDate userAttemptedProductionDate;
    private WeaponFamily userAttemptedWeaponFamily;

    public void init() throws IOException {
        // if id is set -> edit mode
        if (id != null) {
            Optional<Firearm> f = firearmService.findById(id);
            if (f.isPresent()) {
                Firearm fa = f.get();

                // Authorization check: only owner or admin can edit
                boolean isAdmin = request.isUserInRole("admin");
                boolean isOwner = fa.getUser() != null &&
                        request.getUserPrincipal() != null &&
                        fa.getUser().getLogin().equals(request.getUserPrincipal().getName());

                if (!isAdmin && !isOwner) {
                    FacesContext.getCurrentInstance().getExternalContext()
                            .responseSendError(403, "Access denied");
                    return;
                }

                this.name = fa.getName();
                this.caliber = fa.getCaliber();
                this.magazineCapacity = fa.getMagazineCapacity();
                this.productionDate = fa.getProductionDate();
                this.weaponFamily = fa.getWeaponFamily();
                this.version = fa.getVersion();
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
        try {
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
                firearmService.createForCallerPrincipal(f);
                return "/weaponfamily/weaponfamily_view.xhtml?faces-redirect=true&id="
                        + (weaponFamily != null ? weaponFamily.getId() : "");
            } else {
                // update
                Optional<Firearm> of = firearmService.findById(id);
                if (of.isPresent()) {
                    Firearm f = of.get();

                    // Authorization check: only owner or admin can update
                    boolean isAdmin = request.isUserInRole("admin");
                    boolean isOwner = f.getUser() != null &&
                            request.getUserPrincipal() != null &&
                            f.getUser().getLogin().equals(request.getUserPrincipal().getName());

                    if (!isAdmin && !isOwner) {
                        return null; // or redirect to error page
                    }

                    f.setName(name);
                    f.setCaliber(caliber == null ? 0.0 : caliber);
                    f.setMagazineCapacity(magazineCapacity == null ? 0 : magazineCapacity);
                    f.setProductionDate(productionDate);
                    f.setWeaponFamily(weaponFamily);
                    f.setVersion(version); // Set the version for optimistic locking

                    try {
                        firearmService.updateFirearm(f);
                        conflictDetected = false;
                        return "/weaponfamily/weaponfamily_view.xhtml?faces-redirect=true&id="
                                + (weaponFamily != null ? weaponFamily.getId() : "");
                    } catch (EJBException ejbEx) {
                        // Check if the root cause is OptimisticLockException
                        Throwable cause = ejbEx.getCause();
                        if (cause instanceof OptimisticLockException || 
                            cause != null && cause.getClass().getName().contains("OptimisticLockException")) {
                            handleOptimisticLockException();
                            return null; // Stay on the same page to show conflict resolution UI
                        } else {
                            throw ejbEx; // Re-throw if it's not an optimistic lock exception
                        }
                    } catch (OptimisticLockException e) {
                        handleOptimisticLockException();
                        return null; // Stay on the same page to show conflict resolution UI
                    }
                } else {
                    return null;
                }
            }
        } catch (EJBException ejbEx) {
            // Check if the root cause is ConstraintViolationException
            Throwable cause = ejbEx.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException) {
                    handleConstraintViolations((ConstraintViolationException) cause);
                    return null; // Stay on the same page to show validation errors
                }
                cause = cause.getCause();
            }
            throw ejbEx; // Re-throw if it's not a validation exception
        } catch (ConstraintViolationException cve) {
            handleConstraintViolations(cve);
            return null; // Stay on the same page to show validation errors
        }
    }

    private void handleConstraintViolations(ConstraintViolationException cve) {
        FacesContext context = FacesContext.getCurrentInstance();
        
        // Add a global message first to ensure something is visible
        context.addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Validation Error", 
                "Please correct the errors below and try again."));
        
        for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            
            // Extract field name from property path (e.g., "createForCallerPrincipal.firearm.name" -> "name")
            String fieldName = propertyPath;
            if (propertyPath.contains(".")) {
                fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            }
            
            // Map field names to JSF component IDs and add messages
            switch (fieldName) {
                case "name":
                    context.addMessage("firearmForm:name", 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                    break;
                case "caliber":
                    context.addMessage("firearmForm:caliber", 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                    break;
                case "magazineCapacity":
                    context.addMessage("firearmForm:capacity", 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                    break;
                case "productionDate":
                    context.addMessage("firearmForm:production", 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                    break;
                case "weaponFamily":
                    context.addMessage("firearmForm:category", 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
                    break;
                default:
                    // Add as global message if field not recognized
                    context.addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                            fieldName + ": " + message, message));
                    break;
            }
        }
    }

    private void handleOptimisticLockException() {
        // Store user's attempted values
        userAttemptedName = name;
        userAttemptedCaliber = caliber;
        userAttemptedMagazineCapacity = magazineCapacity;
        userAttemptedProductionDate = productionDate;
        userAttemptedWeaponFamily = weaponFamily;

        // Reload current DB state
        Optional<Firearm> reloaded = firearmService.findById(id);
        if (reloaded.isPresent()) {
            currentDbFirearm = reloaded.get();
            
            // Update form fields with DB values
            this.name = currentDbFirearm.getName();
            this.caliber = currentDbFirearm.getCaliber();
            this.magazineCapacity = currentDbFirearm.getMagazineCapacity();
            this.productionDate = currentDbFirearm.getProductionDate();
            this.weaponFamily = currentDbFirearm.getWeaponFamily();
            this.version = currentDbFirearm.getVersion();
        }

        conflictDetected = true;
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Version Conflict Detected!",
                        "Someone else has modified this firearm while you were editing. Please review both versions below and choose which one to keep."));
    }

    public String keepDatabaseVersion() {
        // Already loaded with DB values, just clear conflict flag
        conflictDetected = false;
        return null; // Stay on page with DB values loaded
    }

    public String overrideWithMyChanges() {
        // Restore user's attempted values
        this.name = userAttemptedName;
        this.caliber = userAttemptedCaliber;
        this.magazineCapacity = userAttemptedMagazineCapacity;
        this.productionDate = userAttemptedProductionDate;
        this.weaponFamily = userAttemptedWeaponFamily;
        
        // Update the version to current DB version
        this.version = currentDbFirearm.getVersion();
        
        conflictDetected = false;
        
        // Save again with the updated version
        return save();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCaliber() {
        return caliber;
    }

    public void setCaliber(Double caliber) {
        this.caliber = caliber;
    }

    public Integer getMagazineCapacity() {
        return magazineCapacity;
    }

    public void setMagazineCapacity(Integer magazineCapacity) {
        this.magazineCapacity = magazineCapacity;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public WeaponFamily getWeaponFamily() {
        return weaponFamily;
    }

    public void setWeaponFamily(WeaponFamily weaponFamily) {
        this.weaponFamily = weaponFamily;
    }

    public String getWeaponFamilyIdParam() {
        return weaponFamilyIdParam;
    }

    public void setWeaponFamilyIdParam(String weaponFamilyIdParam) {
        this.weaponFamilyIdParam = weaponFamilyIdParam;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean isConflictDetected() {
        return conflictDetected;
    }

    public Firearm getCurrentDbFirearm() {
        return currentDbFirearm;
    }

    public String getUserAttemptedName() {
        return userAttemptedName;
    }

    public Double getUserAttemptedCaliber() {
        return userAttemptedCaliber;
    }

    public Integer getUserAttemptedMagazineCapacity() {
        return userAttemptedMagazineCapacity;
    }

    public LocalDate getUserAttemptedProductionDate() {
        return userAttemptedProductionDate;
    }

    public WeaponFamily getUserAttemptedWeaponFamily() {
        return userAttemptedWeaponFamily;
    }
}
