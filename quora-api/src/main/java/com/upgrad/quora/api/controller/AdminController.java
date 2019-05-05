package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class AdminController {

    @RequestMapping(method = RequestMethod.DELETE,
            path="/admin/user/{userId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserEntity> deleteUser(@PathVariable("userId") final String userId){
        return new ResponseEntity(HttpStatus.OK);
    }
}
