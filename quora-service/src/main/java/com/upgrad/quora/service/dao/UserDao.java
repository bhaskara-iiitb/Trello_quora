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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {
    @Autowired
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
    public UserEntity getUser(final String userUuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userUuid).getSingleResult();
        } catch (NoResultException e) {
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
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
      
    public UserAuthEntity createAuthToken(final UserAuthEntity authTokenEntity) {
        entityManager.persist(authTokenEntity);
        return authTokenEntity;
    }
      
    public UserAuthEntity getUserAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthByAccessToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {

            return null;
        }
    }

    public void updateUserAuth(final UserAuthEntity updatedUserAuthEntity) {
        entityManager.merge(updatedUserAuthEntity);
    }
      
    public void updateUser(final UserEntity updateUserEntity) {
        entityManager.merge(updateUserEntity);
    }


    public UserAuthEntity getUserAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
