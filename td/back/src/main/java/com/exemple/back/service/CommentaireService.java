package com.exemple.back.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.back.dto.CommentaireCreationDto;
import com.exemple.back.dto.CommentaireDto;
import com.exemple.back.dto.CommentaireMapper;
import com.exemple.back.model.Commentaire;
import com.exemple.back.model.Film;
import com.exemple.back.repository.CommentaireRepository;
import com.exemple.back.repository.FilmRepository;

@Service
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final FilmRepository filmRepository;

    public CommentaireService(CommentaireRepository commentaireRepository, FilmRepository filmRepository) {
        this.commentaireRepository = commentaireRepository;
        this.filmRepository = filmRepository;
    }

    public List<CommentaireDto> findByFilm(Long filmId) {
        if (!filmRepository.existsById(filmId)) {
            throw new FilmNotFoundException(filmId);
        }
        return commentaireRepository.findByFilmIdOrderByDateCreationDescIdDesc(filmId).stream()
                .map(CommentaireMapper::toDto)
                .toList();
    }

    public CommentaireDto findById(Long id) {
        return commentaireRepository.findById(id)
                .map(CommentaireMapper::toDto)
                .orElseThrow(() -> new CommentaireNotFoundException(id));
    }

    @Transactional
    public CommentaireDto save(Long filmId, CommentaireCreationDto dto) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
        valider(dto);
        Commentaire commentaire = CommentaireMapper.toEntity(dto);
        commentaire.setDateCreation(LocalDateTime.now());
        commentaire.setFilm(film);
        film.getCommentaires().add(commentaire);
        return CommentaireMapper.toDto(commentaireRepository.save(commentaire));
    }

    @Transactional
    public void deleteById(Long id) {
        Commentaire commentaire = commentaireRepository.findById(id)
                .orElseThrow(() -> new CommentaireNotFoundException(id));
        commentaire.getFilm().getCommentaires().remove(commentaire);
    }

    private static void valider(CommentaireCreationDto dto) {
        if (dto == null || dto.auteur() == null || dto.auteur().isBlank()) {
            throw new CommentaireInvalideException("L'auteur est obligatoire.");
        }
        if (dto.message() == null || dto.message().isBlank()) {
            throw new CommentaireInvalideException("Le message est obligatoire.");
        }
        if (dto.auteur().trim().length() > 100) {
            throw new CommentaireInvalideException("L'auteur ne doit pas dépasser 100 caractères.");
        }
        if (dto.message().trim().length() > 2000) {
            throw new CommentaireInvalideException("Le message ne doit pas dépasser 2000 caractères.");
        }
    }
}
