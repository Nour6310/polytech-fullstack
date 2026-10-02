package com.exemple.back.dto;

import com.exemple.back.model.Film;

public final class FilmMapper {
    public static FilmDto toDto(Film film) {
        return new FilmDto(
                film.getId(),
                film.getTitre(),
                film.getRealisateur(),
                film.getDateSortie(),
                film.getGenre()
        );
    }
    public static Film toEntity(FilmCreationDto dto) {
        Film film = new Film();
        film.setTitre(dto.titre());
        film.setRealisateur(dto.realisateur());
        film.setDateSortie(dto.dateSortie());
        film.setGenre(dto.genre());
        return film;
    }
}