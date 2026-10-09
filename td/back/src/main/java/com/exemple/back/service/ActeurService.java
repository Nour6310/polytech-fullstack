package com.exemple.back.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.back.dto.ActeurCreationDto;
import com.exemple.back.dto.ActeurDto;
import com.exemple.back.dto.ActeurMapper;
import com.exemple.back.dto.FilmDto;
import com.exemple.back.dto.FilmMapper;
import com.exemple.back.dto.PageDto;
import com.exemple.back.model.Acteur;
import com.exemple.back.model.Role;
import com.exemple.back.repository.ActeurRepository;
import com.exemple.back.repository.FilmRepository;

@Service
public class ActeurService {

    private final ActeurRepository acteurRepository;
    private final FilmRepository filmRepository;

    public ActeurService(ActeurRepository acteurRepository, FilmRepository filmRepository) {
        this.acteurRepository = acteurRepository;
        this.filmRepository = filmRepository;
    }

    public List<ActeurDto> findAll() {
        return acteurRepository.findAll().stream()
                .map(ActeurMapper::toDto)
                .toList();
    }

    private static final Set<String> COLONNES_TRI = Set.of("id", "prenom", "nom");

    public PageDto<ActeurDto> findPage(int page, int taille, String tri, String direction) {
        String colonne = COLONNES_TRI.contains(tri) ? tri : "nom";
        Sort.Direction sens = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(Math.max(page, 0), Math.max(taille, 1), Sort.by(sens, colonne));

        Page<Acteur> resultat = acteurRepository.findAll(pageRequest);
        return new PageDto<>(
                resultat.getContent().stream().map(ActeurMapper::toDto).toList(),
                resultat.getNumber(),
                resultat.getSize(),
                resultat.getTotalElements(),
                resultat.getTotalPages()
        );
    }

    public ActeurDto findById(Long id) {
        if (!acteurRepository.existsById(id)) {
            throw new ActeurNotFoundException(id);
        }
        return ActeurMapper.toDto(acteurRepository.findById(id).get());
    }

    public ActeurDto save(ActeurCreationDto dto) {
        Acteur acteur = ActeurMapper.toEntity(dto);
        return ActeurMapper.toDto(acteurRepository.save(acteur));
    }

    @Transactional
    public ActeurDto update(Long id, ActeurCreationDto dto) {
        if (!acteurRepository.existsById(id)) {
            throw new ActeurNotFoundException(id);
        }
        Acteur acteur = acteurRepository.findById(id).get();
        acteur.setPrenom(dto.prenom());
        acteur.setNom(dto.nom());
        return ActeurMapper.toDto(acteurRepository.save(acteur));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!acteurRepository.existsById(id)) {
            throw new ActeurNotFoundException(id);
        }
        Acteur acteur = acteurRepository.findById(id).get();
        for (Role role : List.copyOf(acteur.getRoles())) {
            role.getFilm().getRoles().remove(role);
        }
        acteurRepository.delete(acteur);
    }
    public List<FilmDto> findFilms(Long acteurId) {
        if (!acteurRepository.existsById(acteurId)) {
            throw new ActeurNotFoundException(acteurId);
        }
        return filmRepository.findFilmsDeActeur(acteurId).stream()
                .map(FilmMapper::toDto)
                .toList();
    }
}