package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;


    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
        UserEntity existingUsername = userDao.getUserByUsername(userEntity.getUsername());
        UserEntity existingEmailid = userDao.getUserByEmail(userEntity.getEmail());

        if(existingUsername != null) {
            throw new SignUpRestrictedException("SGR-001",
                    "Try any other Username, this Username has already been taken");
        }
        if(existingEmailid != null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other" +
                    " emailId");
        }

        String[] encryptedText = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
        UserEntity persistedUserEntity = userDao.createUser(userEntity);

        return persistedUserEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signin(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUserByUsername(username);
        if(userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }

        final String encryptedPassword = passwordCryptographyProvider.encrypt(password, userEntity.getSalt());
        if(encryptedPassword.equals(userEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthEntity userAuthEntity = new UserAuthEntity();
            userAuthEntity.setUser(userEntity);

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(1);

            userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthEntity.setLoginAt(now);
            userAuthEntity.setExpiresAt(expiresAt);
            userAuthEntity.setLoginAt(now);
            userAuthEntity.setUuid(userEntity.getUuid());
            userDao.createAuthToken(userAuthEntity);


            return userAuthEntity;
        }
        else {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }

    /*
        getUserProfile -  This method will return the Details of the Signed in User
     */

    public UserEntity getUserProfile(final String userUuid, final String acesstoken) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(acesstoken);
        if(userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        ZonedDateTime isUserLogout = userAuthEntity.getLogoutAt();
        if (isUserLogout == null) {
            UserEntity userEntity = userDao.getUserByUuid(userUuid);
            if (userEntity == null) {
                throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
            }
            return userEntity;
        }
        
        throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signout(final String accesstoken) throws SignOutRestrictedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(accesstoken);
        if(userAuthEntity == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }

        final ZonedDateTime now = ZonedDateTime.now();
        userAuthEntity.setLogoutAt(now);
        userDao.updateUserAuth(userAuthEntity);

        return userAuthEntity;
    }

}