package com.firearms.firearmcollectionjee.repository.impl;

import com.firearms.firearmcollectionjee.entity.User;
import com.firearms.firearmcollectionjee.repository.api.UserRepositoryInterface;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    return em.createQuery("select u from User u where u.login = :login", User.class)
        .setParameter("login", login)
        .getResultStream()
        .findFirst();
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
    return em.createQuery("select u from User u where u.email = :email", User.class)
        .setParameter("email", email)
        .getResultStream()
        .findFirst();
    }
    
    @Override
    public List<User> findAll() {
    return em.createQuery("select u from User u", User.class).getResultList();
    }
    
    @Override
    public boolean existsById(UUID id) {
    return em.find(User.class, id) != null;
    }
    
    @Override
    public boolean existsByLogin(String login) {
    Long count = em.createQuery("select count(u) from User u where u.login = :login", Long.class)
        .setParameter("login", login)
        .getSingleResult();
    return count != null && count > 0;
    }
    
    @Override
    public boolean existsByEmail(String email) {
    Long count = em.createQuery("select count(u) from User u where u.email = :email", Long.class)
        .setParameter("email", email)
        .getSingleResult();
    return count != null && count > 0;
    }
}