package com.exemple.back.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.exemple.back.dto.FilmCreationDto;
import com.exemple.back.dto.FilmDto;
import com.exemple.back.dto.FilmMapper;
import com.exemple.back.model.Film;
import com.exemple.back.repository.FilmRepository;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }
    public List<FilmDto> findAll() {
        List<Film> films = filmRepository.findAll();
        return films.stream()
                .map(FilmMapper::toDto)
                .toList();
    }
    public FilmDto findById(Long id) {
        if (!filmRepository.existsById(id)) {
            throw new FilmNotFoundException(id);
        }
        Film film = filmRepository.findById(id).get();
        return FilmMapper.toDto(film);
    }
    public FilmDto save(FilmCreationDto dto) {
        Film film = FilmMapper.toEntity(dto);
        Film filmEnregistre = filmRepository.save(film);
        return FilmMapper.toDto(filmEnregistre);
    }
    public FilmDto update(Long id, FilmCreationDto dto) {
        if (!filmRepository.existsById(id)) {
            throw new FilmNotFoundException(id);
        }
        Film film = FilmMapper.toEntity(dto);
        film.setId(id);
        Film filmModifie = filmRepository.save(film);
        return FilmMapper.toDto(filmModifie);
    }
    public void deleteById(Long id) {
        if (!filmRepository.existsById(id)) {
            throw new FilmNotFoundException(id);
        }
        filmRepository.deleteById(id);
    }
}