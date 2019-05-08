package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) {
        return userDao.createUser(userEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity deleteUser(final String uuid, final String authorizationToken) throws AuthorizationFailedException, UserNotFoundException {

        UserEntity userEntity = getUserFromToken(authorizationToken);
        if(!userEntity.getRole().equals("admin")) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }

        UserEntity deleteUserEntity = userDao.getUser(uuid);
        if(deleteUserEntity == null){
            throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
        } else {
            userDao.deleteUser(deleteUserEntity);
        }

        return deleteUserEntity;
    }

    public UserEntity getUserFromToken(String authorizationToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);

        if(userAuthTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if(userAuthTokenEntity.getLogoutAt() != null ){ // && userAuthTokenEntity.getLogoutAt().compareTo(ZonedDateTime.now()) > 0
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        }

        UserEntity userEntity = userAuthTokenEntity.getUser();

        return userEntity;
    }
}
