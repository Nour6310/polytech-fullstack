package com.exemple.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exemple.back.model.Film;

public interface FilmRepository extends JpaRepository<Film, Long> {
}