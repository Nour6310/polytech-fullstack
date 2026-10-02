package com.exemple.back.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.back.dto.FilmCreationDto;
import com.exemple.back.dto.FilmDetailDto;
import com.exemple.back.dto.FilmDto;
import com.exemple.back.dto.FilmMapper;
import com.exemple.back.model.Acteur;
import com.exemple.back.model.Film;
import com.exemple.back.repository.ActeurRepository;
import com.exemple.back.repository.FilmRepository;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final ActeurRepository acteurRepository;

    public FilmService(FilmRepository filmRepository, ActeurRepository acteurRepository) {
        this.filmRepository = filmRepository;
        this.acteurRepository = acteurRepository;
    }
    public List<FilmDto> findAll() {
        List<Film> films = filmRepository.findAll();
        return films.stream()
                .map(FilmMapper::toDto)
                .toList();
    }

    public FilmDto save(FilmCreationDto dto) {
        Film film = FilmMapper.toEntity(dto);
        Film filmEnregistre = filmRepository.save(film);
        return FilmMapper.toDto(filmEnregistre);
    }
        @Transactional
    public FilmDto update(Long id, FilmCreationDto dto) {
        if (!filmRepository.existsById(id)) {
            throw new FilmNotFoundException(id);
        }
        Film film = filmRepository.findById(id).get();
        film.setTitre(dto.titre());
        film.setRealisateur(dto.realisateur());
        film.setDateSortie(dto.dateSortie());
        film.setGenre(dto.genre());
        return FilmMapper.toDto(filmRepository.save(film));
    }
    public void deleteById(Long id) {
        if (!filmRepository.existsById(id)) {
            throw new FilmNotFoundException(id);
        }
        filmRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public FilmDetailDto findById(Long id) {
        if (!filmRepository.existsById(id)) {
            throw new FilmNotFoundException(id);
        }
        Film film = filmRepository.findById(id).get();
        return FilmMapper.toDetailDto(film);
    }
    @Transactional
    public FilmDetailDto ajouterActeur(Long filmId, Long acteurId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
        Acteur acteur = acteurRepository.findById(acteurId)
                .orElseThrow(() -> new ActeurNotFoundException(acteurId));
        film.getActeurs().add(acteur);
        return FilmMapper.toDetailDto(film);
    }

    @Transactional
    public void retirerActeur(Long filmId, Long acteurId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
        Acteur acteur = acteurRepository.findById(acteurId)
                .orElseThrow(() -> new ActeurNotFoundException(acteurId));
        film.getActeurs().remove(acteur);
    }
}