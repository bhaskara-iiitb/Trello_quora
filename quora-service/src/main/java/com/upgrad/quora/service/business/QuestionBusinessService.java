package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionBusinessService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserBusinessService userBusinessService;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(final QuestionEntity questionEntity, final String authorizationToken) throws AuthorizationFailedException {

        UserEntity userEntity = userBusinessService.getUserFromToken(authorizationToken);
        questionEntity.setUser(userEntity);

        questionDao.createQuestion(questionEntity);

        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestions (final String authorizationToken) throws InvalidQuestionException, AuthorizationFailedException {
        userBusinessService.getUserFromToken(authorizationToken);
        List<QuestionEntity> questionEntities = questionDao.getQuestions();
        if(questionEntities == null || questionEntities.size() == 0){
            throw new InvalidQuestionException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return questionEntities;
    }

    public List<QuestionEntity> getAllQuestionsByUser (final String uuid, final String authorizationToken) throws InvalidQuestionException, AuthorizationFailedException, UserNotFoundException {
        userBusinessService.getUserFromToken(authorizationToken);
        UserEntity userEntity = userDao.getUserByUuid(uuid);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }

        List<QuestionEntity> questionEntities = questionDao.getQuestionsByUser(uuid);

        return questionEntities;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity updateQuestion(final QuestionEntity questionEntity, final String authorizationToken) throws AuthorizationFailedException {

        UserEntity userEntity = userBusinessService.getUserFromToken(authorizationToken);

        if(questionEntity.getUser().getId() == userEntity.getId()) {
            questionDao.updateQuestion(questionEntity);
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }

        return questionEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(final String uuid, final String authorizationToken) throws AuthorizationFailedException, InvalidQuestionException {

        UserEntity userEntity = userBusinessService.getUserFromToken(authorizationToken);
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
}
