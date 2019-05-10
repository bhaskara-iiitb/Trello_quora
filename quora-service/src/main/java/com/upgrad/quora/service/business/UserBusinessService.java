package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
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
        if(existingEmailid !=null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other" +
                    " emailId");
        }

        String[] encryptedText = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
        UserEntity persistedUserEntity =  userDao.createUser(userEntity);

        return persistedUserEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signin(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity =  userDao.getUserByUsername(username);
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

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signin(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity =  userDao.getUserByUsername(username);
        if(userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "User not found");
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
            throw new AuthenticationFailedException("ATH-002", "Authentication failed");
        }


    }
}
