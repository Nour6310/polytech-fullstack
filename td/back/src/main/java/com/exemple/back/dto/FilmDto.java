package com.exemple.back.dto;

import java.time.LocalDate;

import com.exemple.back.model.Genre;

public record FilmDto(
        Long id,
        String titre,
        String realisateur,
        LocalDate dateSortie,
        Genre genre
) { }