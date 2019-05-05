package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
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

import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.UUID;


@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @RequestMapping(method = RequestMethod.POST,
            path="/question/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        Timestamp date = new Timestamp(System.currentTimeMillis());
        String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
        questionEntity.setDate(date);

        final QuestionEntity createdQuestionEntity = questionBusinessService.createQuestion(questionEntity, authorization);

        final QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid()).status(null);

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET,
            path="/question/all",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getAllQuestions(@PathVariable("questionId") final String questionId){
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT,
            path="/question/edit/{questionId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> editQuestionContent (@PathVariable("answerId") final String answerId){
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.DELETE,
            path="/question/delete/{questionId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deleteQuestion(@PathVariable("questionId") final String questionId){
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,
            path="question/all/{userId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getAllQuestionsByUser(@PathVariable("userId") final String userId){
        return new ResponseEntity(HttpStatus.OK);
    }
}
