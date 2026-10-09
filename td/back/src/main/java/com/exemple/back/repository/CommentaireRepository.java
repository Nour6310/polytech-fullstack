package com.exemple.back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exemple.back.model.Commentaire;

public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {

    List<Commentaire> findByFilmIdOrderByDateCreationDescIdDesc(Long filmId);
}
