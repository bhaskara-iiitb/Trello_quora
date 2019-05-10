package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;

import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;

import com.upgrad.quora.service.exception.AuthorizationFailedException;

import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import com.upgrad.quora.service.business.AnswerBusinessService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@RestController
@RequestMapping("/")
public class AnswerController {


    @Autowired
    private AnswerBusinessService answerBusinessService;

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @RequestMapping(method = RequestMethod.POST,
            path="questions/{questionId}/answer/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader("authorization") final String authorization,@PathVariable("questionId") final String questionUuid,  AnswerRequest request) throws AuthorizationFailedException, InvalidQuestionException {



        QuestionEntity quesEntity = questionBusinessService.getQuestion(questionUuid);

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setQuestion(quesEntity);
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setAns(request.getAnswer());
        Timestamp date = new Timestamp(System.currentTimeMillis());
        answerEntity.setDate(date);

        final AnswerEntity createdAnswerEntity = answerBusinessService.createAnswer(answerEntity,authorization);
        final AnswerResponse answerResponse = new AnswerResponse().id(createdAnswerEntity.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<AnswerResponse>(answerResponse,HttpStatus.CREATED);

    }


    @RequestMapping(method = RequestMethod.PUT,
            path="answer/edit/{answerId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(@PathVariable("answerId") final String answerUuid, @RequestHeader("authorization") final String authorization, AnswerEditRequest editRequest)throws AuthorizationFailedException{
        // 1. First Get the Answer.
        // 2. Then update the Answer Content
        // Finally update Answer in DB.

        AnswerEntity answerEntity = answerBusinessService.getAnswerByAnswerUuid(answerUuid,authorization);
        answerEntity.setAns(editRequest.getContent());
        Timestamp date = new Timestamp(System.currentTimeMillis());
        answerEntity.setDate(date);

        AnswerEntity updatedAnswerEntity = answerBusinessService.updateAnswer(answerEntity,authorization);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(updatedAnswerEntity.getUuid()).status("ANSWER EDITED");

        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.DELETE,
            path="answer/delete/{answerId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answerUuid, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        // Who all are allowed to delete the answer only user or admin as well.
        //
        AnswerEntity updatedAnswerEntity = answerBusinessService.deleteAnswer(answerUuid, authorization);
        AnswerDeleteResponse deleteResponse = new AnswerDeleteResponse().id(updatedAnswerEntity.getUuid()).status("ANSWER DELETED");

        return new ResponseEntity<AnswerDeleteResponse>(deleteResponse, HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.GET,
            path="answer/all/{questionId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public ResponseEntity<Object> getAllAnswerToQuestion(@PathVariable("questionId") final String questionUuid) throws  InvalidQuestionException {
        // First get question using questionUuid, then find the questionId and for that question Id get all the answers.
        QuestionEntity quesEntity = questionBusinessService.getQuestion(questionUuid);

        // get all question by question ID
        List<AnswerEntity> answerEntities = answerBusinessService.getAllAnswersByQuestionId(quesEntity.getId());

        // Build the answer responses.
        List<AnswerDetailsResponse> answerDetailsResponses = new ArrayList<>();

        for (AnswerEntity answerEntity : answerEntities) {
            answerDetailsResponses.add(
                    new AnswerDetailsResponse()
                            .id(answerEntity.getUuid())
                            .questionContent(answerEntity.getQuestion().getContent())
                             .answerContent(answerEntity.getAns())
            ) ;
        }
        return new ResponseEntity<>(answerDetailsResponses,HttpStatus.OK);

    }

}
