package com.exemple.back.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public Film findById(@PathVariable  Long id){
        return filmService.findById(id);
    }
    @PostMapping
    public ResponseEntity<Film> save(@RequestBody Film film){
        Film nouveaufilm=filmService.save(film);
        URI location=URI.create("/films/"+nouveaufilm.getId());
        return ResponseEntity.created(location).body(nouveaufilm);
    }
    @PutMapping("/{id:\\d+}")
    public Film update(@PathVariable Long id, @RequestBody Film film) {
        return filmService.update(id, film);
    }

    

 }