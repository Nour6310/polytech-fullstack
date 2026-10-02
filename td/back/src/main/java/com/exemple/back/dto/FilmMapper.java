package com.exemple.back.dto;

import java.util.List;

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
    public static FilmDetailDto toDetailDto(Film film) {
        List<ActeurDto> acteurs = film.getActeurs().stream()
                .map(ActeurMapper::toDto)
                .toList();
        return new FilmDetailDto(
                film.getId(),
                film.getTitre(),
                film.getRealisateur(),
                film.getDateSortie(),
                film.getGenre(),
                acteurs
        );
    }
}