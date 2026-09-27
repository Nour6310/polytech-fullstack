package com.exemple.back.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.exemple.back.service.FilmNotFoundException;



@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(FilmNotFoundException.class)
    public ProblemDetail handleFilmNotFound(FilmNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
}