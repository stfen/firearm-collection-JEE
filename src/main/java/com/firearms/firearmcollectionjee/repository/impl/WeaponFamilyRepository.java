package com.firearms.firearmcollectionjee.repository.impl;

import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.repository.api.WeaponFamilyRepositoryInterface;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@NoArgsConstructor(force = true)
public class WeaponFamilyRepository implements WeaponFamilyRepositoryInterface {

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(WeaponFamily weaponFamily) {
        em.persist(weaponFamily);
    }

    @Override
    public void update(WeaponFamily weaponFamily) {
        em.merge(weaponFamily);
    }

    @Override
    public void delete(WeaponFamily weaponFamily) {
        WeaponFamily managed = em.find(WeaponFamily.class, weaponFamily.getId());
        if (managed != null) {
            // Force load the firearms collection so cascade delete works
            if (managed.getFirearms() != null) {
                managed.getFirearms().size();
            }
            em.remove(managed);
        }
    }

    @Override
    public Optional<WeaponFamily> findById(UUID id) {
        return Optional.ofNullable(em.find(WeaponFamily.class, id));
    }

    @Override
    public List<WeaponFamily> findAll(){
        return em.createQuery("select w from WeaponFamily w", WeaponFamily.class).getResultList();
    }
}
