package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;

import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;

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



@RestController
@RequestMapping("/")
public class AnswerController {


    @Autowired
    private AnswerBusinessService answerBusinessService;

    @RequestMapping(method = RequestMethod.POST,
            path="questions/{questionId}/answer/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader("authorization") final String authorization,@PathVariable("questionId") final String questionId,  AnswerRequest request) throws AuthorizationFailedException {

        final AnswerResponse answerResponse = new AnswerResponse().id("1234").status("ANSWER CREATED");

        AnswerEntity answerEntity = new AnswerEntity();
        
        // get the question for given question ID.



        // Now create Answer for question and persist.


        // return Answer Id with Response Code.


        return new ResponseEntity<AnswerResponse>(answerResponse,HttpStatus.CREATED);

    }


    @RequestMapping(method = RequestMethod.PUT,
            path="answer/edit/{answerId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<AnswerEditResponse> editAnswerContent(@PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String authorization, AnswerEditRequest editRequest)throws AuthorizationFailedException{
        final AnswerEditResponse answerEditResponse = new AnswerEditResponse().id("1234").status("ANSWER CHANGED SUCCESSFULLY");
        return new ResponseEntity<>(answerEditResponse,HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.DELETE,
            path="answer/delete/{answerId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException{
        final AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id("1234").status("ANSWER DELETED SUCCESSFULLY");
        return new ResponseEntity<>(answerDeleteResponse,HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.GET,
            path="answer/all/{questionId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDetailsResponse> getAllAnswerToQuestion(@PathVariable("questionId") final String questionId){
        final AnswerDetailsResponse answerDetailResponse = new AnswerDetailsResponse().id("1234").questionContent("This is First Question").answerContent("This is First Question Answer");
        return new ResponseEntity<>(answerDetailResponse,HttpStatus.OK);

    }

}
