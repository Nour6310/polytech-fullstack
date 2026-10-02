package com.exemple.back.dto;

import java.time.LocalDate;
import java.util.List;

import com.exemple.back.model.Genre;

public record FilmDetailDto(
        Long id,
        String titre,
        String realisateur,
        LocalDate dateSortie,
        Genre genre,
        List<ActeurDto> acteurs
) { }