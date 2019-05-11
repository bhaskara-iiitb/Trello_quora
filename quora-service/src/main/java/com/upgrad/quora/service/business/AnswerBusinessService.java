package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerBusinessService {
    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;


    @Autowired
    private AnswerDao answerDao;
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(final AnswerEntity answerEntity, final String authorizationToken) throws AuthorizationFailedException {

        UserEntity userEntity = getUserFromToken(authorizationToken);
        answerEntity.setUser(userEntity);
        answerDao.createAnswer(answerEntity);
        return answerEntity;
    }



    public AnswerEntity getAnswerByAnswerUuid(final String answerUuid, final String authorizationToken) throws AuthorizationFailedException,AnswerNotFoundException {

        getUserFromToken(authorizationToken);
        AnswerEntity answerEntity = answerDao.getAnswerByUuid(answerUuid);
        if(answerEntity == null){
            throw new AnswerNotFoundException("ANS-001", "Entered Answer uuid does not exist");
        }
        return answerEntity;
    }



    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity updateAnswer(final AnswerEntity answerEntity, final String authorizationToken) throws AuthorizationFailedException {

        UserEntity userEntity = getUserFromToken(authorizationToken);

        if(answerEntity.getUser().getId() == userEntity.getId()) {
            answerDao.updateAnswer(answerEntity);
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Only the Answer owner can edit the Answer");
        }

        return answerEntity;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(final String answerUuid, final String authorizationToken) throws AuthorizationFailedException,AnswerNotFoundException {

        UserEntity userEntity = getUserFromToken(authorizationToken);
        AnswerEntity answerEntity = answerDao.getAnswerByUuid(answerUuid);

        if(answerEntity == null){
            throw new AnswerNotFoundException("ANS-001", "Entered Answer uuid does not exist");
        }

        if(userEntity.getUuid().equals(answerEntity.getUser().getUuid()) || userEntity.getRole().equals("admin")) {
            answerDao.deleteAnswer(answerEntity);
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Only the Answer owner or Admin can Delete this Answer");
        }

        return answerEntity;
    }

    public List<AnswerEntity> getAllAnswersByQuestionId (final Integer id,final String authorizationToken) throws AuthorizationFailedException,InvalidQuestionException{
        UserAuthEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);

        if(userAuthTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if(userAuthTokenEntity.getLogoutAt() != null ){ // && userAuthTokenEntity.getLogoutAt().compareTo(ZonedDateTime.now()) > 0
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }

        List<AnswerEntity> answerEntities = answerDao.getAnswersByQuestionId(id);
        if(answerEntities == null){
            throw new InvalidQuestionException("ANS-002", "No Answer with specified Question Id exist.");
        }
        return answerEntities;
    }


    public UserEntity getUserFromToken(String authorizationToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);

        if(userAuthTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if(userAuthTokenEntity.getLogoutAt() != null ){ // && userAuthTokenEntity.getLogoutAt().compareTo(ZonedDateTime.now()) > 0
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }

        UserEntity userEntity = userAuthTokenEntity.getUser();

        return userEntity;
    }
}
