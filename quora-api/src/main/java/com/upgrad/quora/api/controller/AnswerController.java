package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/")
public class AnswerController {

    @RequestMapping(method = RequestMethod.POST,
            path="/questions/{questionId}/answer/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEntity> createAnswer(@PathVariable("questionId") final String questionId){
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT,
            path="/answer/edit/{answerId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEntity> editAnswerContent(@PathVariable("answerId") final String answerId){
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.DELETE,
            path="/answer/delete/{answerId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEntity> deleteAnswer(@PathVariable("answerId") final String answerId){
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET,
            path="/answer/all/{questionId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getAllAnswer(@PathVariable("questionId") final String questionId){
        return new ResponseEntity(HttpStatus.OK);
    }

}
