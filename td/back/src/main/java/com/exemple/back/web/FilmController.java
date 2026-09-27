package com.exemple.back.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.back.model.Film;
import com.exemple.back.service.FilmService;

@RestController
@RequestMapping("/films")
public class FilmController{
    private final FilmService filmService ;
    public FilmController(FilmService filmService){
        this.filmService=filmService;    }
    @GetMapping
    public List<Film> findAll(){
        return filmService.findAll();
    }
    @GetMapping("/{id:\\d+}")
    public Film findById(Long id){
        return filmService.findById(id);
    }

    

 }