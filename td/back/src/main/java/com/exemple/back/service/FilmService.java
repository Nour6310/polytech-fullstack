package com.exemple.back.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.back.dto.ActeurDto;
import com.exemple.back.dto.ActeurMapper;
import com.exemple.back.dto.FilmCreationDto;
import com.exemple.back.dto.FilmDetailDto;
import com.exemple.back.dto.FilmDto;
import com.exemple.back.dto.FilmMapper;
import com.exemple.back.dto.RoleCreationDto;
import com.exemple.back.model.Acteur;
import com.exemple.back.model.Film;
import com.exemple.back.model.Role;
import com.exemple.back.repository.ActeurRepository;
import com.exemple.back.repository.FilmRepository;
import com.exemple.back.repository.RoleRepository;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final ActeurRepository acteurRepository;
    private final RoleRepository roleRepository;

    public FilmService(FilmRepository filmRepository, ActeurRepository acteurRepository,
            RoleRepository roleRepository) {
        this.filmRepository = filmRepository;
        this.acteurRepository = acteurRepository;
        this.roleRepository = roleRepository;
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
    @Transactional
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
    public FilmDetailDto ajouterActeur(Long filmId, Long acteurId, RoleCreationDto dto) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
        Acteur acteur = acteurRepository.findById(acteurId)
                .orElseThrow(() -> new ActeurNotFoundException(acteurId));
        String personnage = nettoyer(dto == null ? null : dto.personnage());

        Role role = roleRepository.findByFilmIdAndActeurId(filmId, acteurId).orElse(null);
        if (role == null) {
            role = roleRepository.save(new Role(film, acteur, personnage));
            film.getRoles().add(role);
            acteur.getRoles().add(role);
        } else if (personnage != null) {
            role.setPersonnage(personnage);
        }
        return FilmMapper.toDetailDto(film);
    }

    private static String nettoyer(String personnage) {
        if (personnage == null || personnage.isBlank()) {
            return null;
        }
        return personnage.trim();
    }

    @Transactional
    public void retirerActeur(Long filmId, Long acteurId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
        Acteur acteur = acteurRepository.findById(acteurId)
                .orElseThrow(() -> new ActeurNotFoundException(acteurId));
        roleRepository.findByFilmIdAndActeurId(filmId, acteurId).ifPresent(role -> {
            film.getRoles().remove(role);
            acteur.getRoles().remove(role);
        });
    }
    public List<ActeurDto> findActeurs(Long filmId) {
        if (!filmRepository.existsById(filmId)) {
            throw new FilmNotFoundException(filmId);
        }
        return acteurRepository.findByRolesFilmId(filmId).stream()
                .map(ActeurMapper::toDto)
                .toList();
    }
    
}