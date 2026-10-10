package com.firearms.firearmcollectionjee.view.firearm;

import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.model.firearm.FirearmModel;
import com.firearms.firearmcollectionjee.service.FirearmService;
import com.firearms.firearmcollectionjee.component.ModelFunctionFactory;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@ViewScoped
@Named
public class FirearmView implements Serializable {

    private final FirearmService firearmService;
    private final ModelFunctionFactory modelFactory;

    @Setter
    @Getter
    private UUID id;

    @Getter
    private FirearmModel firearm;

    private final HttpServletRequest request;

    @Inject
    public FirearmView(FirearmService firearmService, ModelFunctionFactory modelFactory, HttpServletRequest request) {
        this.firearmService = firearmService;
        this.modelFactory = modelFactory;
        this.request = request;
    }

    public void init() throws IOException {
        Optional<Firearm> firearm = firearmService.findById(id);
        if (firearm.isPresent()) {
            Firearm f = firearm.get();
            if (request.isUserInRole("admin")
                    || (f.getUser() != null && f.getUser().getLogin().equals(request.getUserPrincipal().getName()))) {
                this.firearm = modelFactory.firearmToModel().apply(f);
            } else {
                FacesContext.getCurrentInstance().getExternalContext()
                        .responseSendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            }
        } else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND,
                    "Firearm not found");
        }
    }

}
