package com.exemple.back.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.exemple.back.service.ActeurNotFoundException;
import com.exemple.back.service.CommentaireInvalideException;
import com.exemple.back.service.CommentaireNotFoundException;
import com.exemple.back.service.FilmNotFoundException;



@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(FilmNotFoundException.class)
    public ProblemDetail handleFilmNotFound(FilmNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
    @ExceptionHandler(ActeurNotFoundException.class)
    public ProblemDetail handleActeurNotFound(ActeurNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
    @ExceptionHandler(CommentaireNotFoundException.class)
    public ProblemDetail handleCommentaireNotFound(CommentaireNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
    @ExceptionHandler(CommentaireInvalideException.class)
    public ProblemDetail handleCommentaireInvalide(CommentaireInvalideException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}