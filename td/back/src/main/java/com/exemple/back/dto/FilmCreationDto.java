package com.exemple.back.dto;

import java.time.LocalDate;

import com.exemple.back.model.Genre;

public record FilmCreationDto(
        String titre,
        String realisateur,
        LocalDate dateSortie,
        Genre genre
) { }