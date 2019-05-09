package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getUserByUsername(final String username) {

        try {
            return entityManager.createNamedQuery(
                    "userByUsername", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
        }
        catch(NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserByEmail(final String email) {

        try {
            return entityManager.createNamedQuery(
                    "userByEmail", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
        }
        catch(NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserByUsername(final String username) {

        try {
            return entityManager.createNamedQuery(
                    "userByUsername", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
        }
        catch(NoResultException nre) {
            return null;
        }
    }

    public UserAuthEntity createAuthToken(final UserAuthEntity authTokenEntity) {
        entityManager.persist(authTokenEntity);
        return authTokenEntity;
    }

}
