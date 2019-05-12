package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionEditResponse;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @RequestMapping(method = RequestMethod.POST,
            path="/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") final String authorization, final QuestionRequest questionRequest) throws AuthorizationFailedException {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        Timestamp date = new Timestamp(System.currentTimeMillis());
        questionEntity.setDate(date);

        final QuestionEntity createdQuestionEntity = questionBusinessService.createQuestion(questionEntity, authorization);

        final QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET,
            path="/all",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        List<QuestionEntity> questionEntities = questionBusinessService.getAllQuestions(authorization);
        List<QuestionDetailsResponse> questionResponses = entitiesToResponse(questionEntities);

        return new ResponseEntity(questionResponses, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT,
            path="/edit/{questionId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent (@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionId, final QuestionEditRequest questionRequest) throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity questionEntity = questionBusinessService.getQuestion(questionId);
        questionEntity.setContent(questionRequest.getContent());
        Timestamp date = new Timestamp(System.currentTimeMillis());
        questionEntity.setDate(date);

        QuestionEntity updatedQuestionEntity = questionBusinessService.updateQuestion(questionEntity, authorization);
        QuestionEditResponse questionResponse = new QuestionEditResponse().id(updatedQuestionEntity.getUuid()).status("QUESTION EDITED");

        return new ResponseEntity<QuestionEditResponse>(questionResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.DELETE,
            path="/delete/{questionId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity updatedQuestionEntity = questionBusinessService.deleteQuestion(questionId, authorization);
        QuestionDeleteResponse questionResponse = new QuestionDeleteResponse().id(updatedQuestionEntity.getUuid()).status("QUESTION DELETED");

        return new ResponseEntity<QuestionDeleteResponse>(questionResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,
            path="/all/{userId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@RequestHeader("authorization") final String authorization, @PathVariable("userId") final String userId) throws AuthorizationFailedException, InvalidQuestionException {
        List<QuestionEntity> questionEntities = questionBusinessService.getAllQuestionsByUser(userId, authorization);
        List<QuestionDetailsResponse> questionResponses = entitiesToResponse(questionEntities);

        return new ResponseEntity(questionResponses, HttpStatus.OK);
    }

    private List<QuestionDetailsResponse> entitiesToResponse(List<QuestionEntity> questionEntities) {
        List<QuestionDetailsResponse> questionResponses = new ArrayList<>();
        for (QuestionEntity questionEntity : questionEntities) {
            questionResponses.add(
                    new QuestionDetailsResponse()
                            .id(questionEntity.getUuid())
                            .content(questionEntity.getContent())
            ) ;
        }

        return questionResponses;
    }
}
