package com.firearms.firearmcollectionjee.service;

import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.entity.User;
import com.firearms.firearmcollectionjee.entity.enums.UserRoles;
import com.firearms.firearmcollectionjee.interceptor.Loggable;
import com.firearms.firearmcollectionjee.repository.api.FirearmRepositoryInterface;
import com.firearms.firearmcollectionjee.repository.api.UserRepositoryInterface;
import com.firearms.firearmcollectionjee.repository.api.WeaponFamilyRepositoryInterface;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJBAccessException;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class FirearmService {

    private final FirearmRepositoryInterface firearmRepository;
    private final UserRepositoryInterface userRepository;
    private final WeaponFamilyRepositoryInterface weaponFamilyRepository;
    private final SecurityContext securityContext;

    @Inject
    public FirearmService(FirearmRepositoryInterface firearmRepository,
                          UserRepositoryInterface userRepository,
                          WeaponFamilyRepositoryInterface weaponFamilyRepository,
                          @SuppressWarnings("CdiInjectionPointsInspection") SecurityContext securityContext
    ) {
        this.firearmRepository = firearmRepository;
        this.userRepository = userRepository;
        this.weaponFamilyRepository = weaponFamilyRepository;
        this.securityContext = securityContext;
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void createFirearm(Firearm firearm) {
        if (firearmRepository.findById(firearm.getId()).isPresent()) {
            throw new IllegalArgumentException("Firearm already exists.");
        }
        if (weaponFamilyRepository.findById(firearm.getWeaponFamily().getId()).isEmpty()) {
            throw new IllegalArgumentException("WeaponFamily does not exists.");
        }
        firearmRepository.create(firearm);
    }

    @Loggable
    @RolesAllowed(UserRoles.USER)
    public void createForCallerPrincipal(Firearm firearm) {
        User user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName())
                .orElseThrow(IllegalStateException::new);

        firearm.setUser(user);
        createFirearm(firearm);
    }

    @Loggable
    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public void updateFirearm(Firearm firearm) {
        checkAdminRoleOrOwner(firearmRepository.findById(firearm.getId()));
        firearmRepository.update(firearm);
    }

    @Loggable
    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public void deleteFirearm(UUID id) {
        checkAdminRoleOrOwner(firearmRepository.findById(id));
        if (id == null) {
            throw new IllegalArgumentException("Firearm ID cannot be null");
        }
        firearmRepository.findById(id).ifPresent(firearmRepository::delete);
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public Optional<Firearm> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return firearmRepository.findById(id);
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public Optional<Firearm> find(User user, UUID id) {
        return firearmRepository.findByIdAndUser(id, user);
    }


    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public Optional<Firearm> findForCallerPrincipal(UUID id) {
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return findById(id);
        }
        User user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName())
                .orElseThrow(IllegalStateException::new);
        return find(user, id);
    }


    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public List<Firearm> findAll() {
        return firearmRepository.findAll();
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public List<Firearm> findAll(User user) {
        return firearmRepository.findAllByUser(user);
    }


    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public List<Firearm> findAllForCallerPrincipal() {
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return findAll();
        }
        User user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName())
                .orElseThrow(IllegalStateException::new);
        return findAll(user);
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public Optional<List<Firearm>> findAllByUser(UUID id) {
        return userRepository.findById(id)
                .map(firearmRepository::findAllByUser);
    }

    @RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
    public Optional<List<Firearm>> findAllByWeaponFamily(UUID id) {
        return weaponFamilyRepository.findById(id)
                .map(firearmRepository::findAllByWeaponFamily);
    }

    private void checkAdminRoleOrOwner(Optional<Firearm> firearm) throws EJBAccessException {
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return;
        }
        if (securityContext.isCallerInRole(UserRoles.USER)
                && firearm.isPresent()
                && firearm.get().getUser().getLogin().equals(securityContext.getCallerPrincipal().getName())) {
            return;
        }
        throw new EJBAccessException("Caller not authorized.");
    }


}
