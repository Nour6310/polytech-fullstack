package com.exemple.back.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.back.dto.ActeurCreationDto;
import com.exemple.back.dto.ActeurDto;
import com.exemple.back.dto.ActeurMapper;
import com.exemple.back.model.Acteur;
import com.exemple.back.model.Film;
import com.exemple.back.repository.ActeurRepository;

@Service
public class ActeurService {

    private final ActeurRepository acteurRepository;

    public ActeurService(ActeurRepository acteurRepository) {
        this.acteurRepository = acteurRepository;
    }

    public List<ActeurDto> findAll() {
        return acteurRepository.findAll().stream()
                .map(ActeurMapper::toDto)
                .toList();
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
        for (Film film : acteur.getFilms()) {
            film.getActeurs().remove(acteur);
        }
        acteurRepository.delete(acteur);
    }
}