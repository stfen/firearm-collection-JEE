package com.firearms.firearmcollectionjee.repository.impl;

import com.firearms.firearmcollectionjee.entity.User;
import com.firearms.firearmcollectionjee.repository.api.UserRepositoryInterface;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of UserRepositoryInterface.
 * Handles all CRUD operations for users using the in-memory data storage.
 */
@Dependent
public class UserRepository implements UserRepositoryInterface {
    
    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public void create(User user) {
        em.persist(user);
    }

    @Override
    public void update(User user) {
        em.merge(user);
    }

    @Override
    public void delete(User entity) {
        em.remove(em.find(User.class, entity.getId()));
    }


    @Override
    public Optional<User> findById(UUID id) {
    return Optional.ofNullable(em.find(User.class, id));
    }
    
    @Override
    public Optional<User> findByLogin(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);
        query.select(user).where(cb.equal(user.get("login"), login));
        return em.createQuery(query).getResultStream().findFirst();
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);
        query.select(user).where(cb.equal(user.get("email"), email));
        return em.createQuery(query).getResultStream().findFirst();
    }
    
    @Override
    public List<User> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);
        query.select(user);
        return em.createQuery(query).getResultList();
    }
    
    @Override
    public boolean existsById(UUID id) {
    return em.find(User.class, id) != null;
    }
    
    @Override
    public boolean existsByLogin(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<User> user = query.from(User.class);
        query.select(cb.count(user)).where(cb.equal(user.get("login"), login));
        Long count = em.createQuery(query).getSingleResult();
        return count != null && count > 0;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<User> user = query.from(User.class);
        query.select(cb.count(user)).where(cb.equal(user.get("email"), email));
        Long count = em.createQuery(query).getSingleResult();
        return count != null && count > 0;
    }
}