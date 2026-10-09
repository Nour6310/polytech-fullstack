package com.exemple.back.dto;

import java.util.Comparator;
import java.util.List;

import com.exemple.back.model.Film;
import com.exemple.back.model.Role;

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
        List<ActeurRoleDto> acteurs = film.getRoles().stream()
                .sorted(Comparator.comparing((Role r) -> r.getActeur().getNom())
                        .thenComparing(r -> r.getActeur().getPrenom()))
                .map(FilmMapper::toActeurRoleDto)
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
    public static ActeurRoleDto toActeurRoleDto(Role role) {
        return new ActeurRoleDto(
                role.getActeur().getId(),
                role.getActeur().getPrenom(),
                role.getActeur().getNom(),
                role.getPersonnage()
        );
    }
}