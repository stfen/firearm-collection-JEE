package com.firearms.firearmcollectionjee.repository.impl;

import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.entity.User;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.repository.api.FirearmRepositoryInterface;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Dependent
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
    public Optional<Firearm> findByIdAndUser(UUID id, User user) {
        try {
            return Optional.of(em.createQuery("select c from Firearm c where c.id = :id and c.user = :user", Firearm.class)
                    .setParameter("user", user)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
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
