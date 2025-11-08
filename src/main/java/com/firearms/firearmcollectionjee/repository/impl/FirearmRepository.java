package com.firearms.firearmcollectionjee.repository.impl;

import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.entity.User;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.repository.api.FirearmRepositoryInterface;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
@NoArgsConstructor(force = true)
public class FirearmRepository implements FirearmRepositoryInterface {

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(Firearm firearm) {
        em.persist(firearm);
    }

    @Override
    public void update(Firearm firearm) {
        em.merge(firearm);
    }

    @Override
    public void delete(Firearm firearm) {
        em.remove(em.find(Firearm.class, firearm.getId()));
    }


    @Override
    public Optional<Firearm> findById(UUID id) {
        return Optional.ofNullable(em.find(Firearm.class, id));
    }

    @Override
    public List<Firearm> findAllByUser(User user) {
        return em.createQuery("select f from Firearm f where f.user = :user", Firearm.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<Firearm> findAllByWeaponFamily(WeaponFamily weaponFamily) {
        return em.createQuery("select f from Firearm f where f.weaponFamily = :weapon_family", Firearm.class)
                .setParameter("weapon_family", weaponFamily)
                .getResultList();
    }

    @Override
    public List<Firearm> findAll(){
        return em.createQuery("select f from Firearm f", Firearm.class).getResultList();
    }
}
