package com.firearms.firearmcollectionjee.controller.jsf;

import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.model.firearm.FirearmModel;
import com.firearms.firearmcollectionjee.service.FirearmService;
import com.firearms.firearmcollectionjee.component.ModelFunctionFactory;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
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


    @Inject
    public FirearmView(FirearmService firearmService, ModelFunctionFactory modelFactory) {
        this.firearmService = firearmService;
        this.modelFactory = modelFactory;
    }

    public void init() throws IOException {
        Optional<Firearm> firearm = firearmService.findById(id);
        if(firearm.isPresent()) {
            this.firearm = modelFactory.firearmToModel().apply(firearm.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Firearm not found");
        }
    }

}
