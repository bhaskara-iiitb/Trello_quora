package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class QuestionBusinessService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(final QuestionEntity questionEntity, final String authorizationToken) throws AuthorizationFailedException {

        UserEntity userEntity = getUserFromToken(authorizationToken);
        questionEntity.setUser(userEntity);

        questionDao.createQuestion(questionEntity);

        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestions (final String authorizationToken) throws InvalidQuestionException, AuthorizationFailedException {
        getUserFromToken(authorizationToken);
        List<QuestionEntity> questionEntity = questionDao.getQuestions();
        if(questionEntity == null || questionEntity.size() == 0){

            throw new InvalidQuestionException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestionsByUser (final String uuid, final String authorizationToken) throws InvalidQuestionException, AuthorizationFailedException {
        getUserFromToken(authorizationToken);
        List<QuestionEntity> questionEntity = questionDao.getQuestionsByUser(uuid);
        if(questionEntity == null || questionEntity.size() == 0){
            throw new InvalidQuestionException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return questionEntity;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity updateQuestion(final QuestionEntity questionEntity, final String authorizationToken) throws AuthorizationFailedException {

        UserEntity userEntity = getUserFromToken(authorizationToken);

        if(questionEntity.getUser().getId() == userEntity.getId()) {
            questionDao.updateQuestion(questionEntity);
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }

        return questionEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(final String uuid, final String authorizationToken) throws AuthorizationFailedException, InvalidQuestionException {

        UserEntity userEntity = getUserFromToken(authorizationToken);
        QuestionEntity questionEntity = questionDao.getQuestion(uuid);
        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        if(userEntity.getUuid().equals(questionEntity.getUser().getUuid()) || userEntity.getRole().equals("admin")) {
            questionDao.deleteQuestion(questionEntity);
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }

        return questionEntity;
    }

    public QuestionEntity getQuestion(String uuid) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.getQuestion(uuid);

        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        return questionEntity;
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
