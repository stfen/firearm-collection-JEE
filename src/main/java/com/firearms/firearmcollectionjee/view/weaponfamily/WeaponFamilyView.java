package com.firearms.firearmcollectionjee.view.weaponfamily;

import com.firearms.firearmcollectionjee.model.firearm.FirearmModel;
import com.firearms.firearmcollectionjee.model.weaponfamily.WeaponFamilyModel;
import com.firearms.firearmcollectionjee.service.FirearmService;
import com.firearms.firearmcollectionjee.service.WeaponFamilyService;
import com.firearms.firearmcollectionjee.component.ModelFunctionFactory;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ViewScoped
@Named
public class WeaponFamilyView implements Serializable {

    private final WeaponFamilyService weaponFamilyService;
    private final FirearmService firearmService;
    private final ModelFunctionFactory modelFactory;

    private UUID id;
    private String idParam;

    private WeaponFamilyModel weaponFamily;
    private List<FirearmModel> firearms = Collections.emptyList();

    private final HttpServletRequest request;

    @Inject
    public WeaponFamilyView(WeaponFamilyService weaponFamilyService,
            FirearmService firearmService,
            ModelFunctionFactory modelFactory,
            HttpServletRequest request) {
        this.weaponFamilyService = weaponFamilyService;
        this.firearmService = firearmService;
        this.modelFactory = modelFactory;
        this.request = request;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setIdParam(String idParam) {
        this.idParam = idParam;
    }

    public String getIdParam() {
        return idParam;
    }

    public WeaponFamilyModel getWeaponFamily() {
        return weaponFamily;
    }

    public List<FirearmModel> getFirearms() {
        return firearms;
    }

    public void init() throws IOException {
        // debug log to help trace why id may be missing or lookup fails
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .log("WeaponFamilyView.init called: id=" + id + ", idParam=" + idParam);
        } catch (Exception ignored) {
            // ignore logging failures
        }
        if (id == null && idParam != null && !idParam.isBlank()) {
            try {
                id = UUID.fromString(idParam);
            } catch (IllegalArgumentException ex) {
                FacesContext.getCurrentInstance().getExternalContext()
                        .responseSendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
                return;
            }
        }

        if (id == null) {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_BAD_REQUEST, "Missing id");
            return;
        }

        Optional.ofNullable(weaponFamilyService.findById(id).orElse(null))
                .map(modelFactory.weaponFamilyToModel())
                .ifPresentOrElse(wfm -> {
                    this.weaponFamily = wfm;
                    var fFn = modelFactory.firearmToModel();
                    this.firearms = firearmService.findAllByWeaponFamily(id)
                            .orElse(Collections.emptyList())
                            .stream()
                            .filter(firearm -> request.isUserInRole("admin") || (firearm.getUser() != null
                                    && firearm.getUser().getLogin().equals(request.getUserPrincipal().getName())))
                            .map(fFn)
                            .toList();
                }, () -> {
                    try {
                        FacesContext.getCurrentInstance().getExternalContext()
                                .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Weapon family not found");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public String deleteFirearm(String id) {
        if (id == null)
            return null;
        try {
            UUID uuid = UUID.fromString(id);
            firearmService.deleteFirearm(uuid);
            // refresh list
            if (weaponFamily != null) {
                var fFn = modelFactory.firearmToModel();
                this.firearms = firearmService.findAllByWeaponFamily(this.weaponFamily.getId())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(fFn)
                        .toList();
            }
        } catch (IllegalArgumentException ex) {
            // ignore
        }
        return null;
    }
}
