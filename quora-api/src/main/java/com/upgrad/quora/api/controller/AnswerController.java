package com.upgrad.quora.api.controller;
import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/")
public class AnswerController {

    @RequestMapping(method = RequestMethod.POST,
            path="/questions/{questionId}/answer/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization, AnswerRequest request) throws AuthorizationFailedException {

        final AnswerResponse answerResponse = new AnswerResponse().id("1234").status("ANSWER CREATED");
        // get the question for given question ID.


        return new ResponseEntity<AnswerResponse>(answerResponse,HttpStatus.CREATED);

    }


    @RequestMapping(method = RequestMethod.PUT,
            path="/answer/edit/{answerId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(@PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String authorization, AnswerEditRequest editRequest)throws AuthorizationFailedException{
        final AnswerEditResponse answerEditResponse = new AnswerEditResponse().id("1234").status("ANSWER CHANGED SUCCESSFULLY");
        return new ResponseEntity<>(answerEditResponse,HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.DELETE,
            path="/answer/delete/{answerId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException{
        final AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id("1234").status("ANSWER DELETED SUCCESSFULLY");
        return new ResponseEntity<>(answerDeleteResponse,HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.GET,
            path="/answer/all/{questionId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDetailsResponse> getAllAnswerToQuestion(@PathVariable("questionId") final String questionId){
        final AnswerDetailsResponse answerDetailResponse = new AnswerDetailsResponse().id("1234").questionContent("This is First Question").answerContent("This is First Question Answer");
        return new ResponseEntity<>(answerDetailResponse,HttpStatus.OK);

    }

}
