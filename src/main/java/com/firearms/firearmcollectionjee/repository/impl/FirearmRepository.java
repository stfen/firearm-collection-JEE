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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Firearm> query = cb.createQuery(Firearm.class);
            Root<Firearm> firearm = query.from(Firearm.class);
            query.select(firearm).where(
                cb.and(
                    cb.equal(firearm.get("id"), id),
                    cb.equal(firearm.get("user"), user)
                )
            );
            return Optional.of(em.createQuery(query).getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Firearm> findAllByUser(User user) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Firearm> query = cb.createQuery(Firearm.class);
        Root<Firearm> firearm = query.from(Firearm.class);
        query.select(firearm).where(cb.equal(firearm.get("user"), user));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<Firearm> findAllByWeaponFamily(WeaponFamily weaponFamily) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Firearm> query = cb.createQuery(Firearm.class);
        Root<Firearm> firearm = query.from(Firearm.class);
        query.select(firearm).where(cb.equal(firearm.get("weaponFamily"), weaponFamily));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<Firearm> findAll(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Firearm> query = cb.createQuery(Firearm.class);
        Root<Firearm> firearm = query.from(Firearm.class);
        query.select(firearm);
        return em.createQuery(query).getResultList();
    }
}
