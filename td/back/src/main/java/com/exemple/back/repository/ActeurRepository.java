package com.exemple.back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exemple.back.model.Acteur;
public interface ActeurRepository extends JpaRepository<Acteur, Long> {

    List<Acteur> findByRolesFilmId(Long filmId);
}