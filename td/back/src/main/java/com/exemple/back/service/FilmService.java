package com.exemple.back.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.exemple.back.model.Film;
import com.exemple.back.repository.FilmRepository;

@Service
public class FilmService {
    private final FilmRepository filmRepository ;
    public  FilmService( FilmRepository filmRepository){
        this.filmRepository=filmRepository;
    }
    public List<Film> findAll(){
        return filmRepository.findAll();
    }
    public Film findById(Long id){
        if (filmRepository.existsById(id)==false){
            throw new FilmNotFoundException(id);
        }
        return filmRepository.findById(id);
    }
    public Film save(Film film ){
        film.setId(null);
        return filmRepository.save(film);
        }
    public void deleteById(Long id){
        if(filmRepository.existsById(id)==false){
            throw new FilmNotFoundException(id);
            }
        filmRepository.deleteById(id);   
    }
    public Film update(Long id,Film film){
        if(filmRepository.existsById(id)==false){
            throw new FilmNotFoundException(id);
        }
        film.setId(id);
        return filmRepository.save(film);
    }
}
