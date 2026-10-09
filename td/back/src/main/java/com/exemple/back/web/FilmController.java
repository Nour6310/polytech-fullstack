package com.exemple.back.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.back.dto.ActeurDto;
import com.exemple.back.dto.FilmCreationDto;
import com.exemple.back.dto.FilmDetailDto;
import com.exemple.back.dto.FilmDto;
import com.exemple.back.dto.RoleCreationDto;
import com.exemple.back.service.FilmService;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<FilmDto> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id:\\d+}")
    public FilmDetailDto findById(@PathVariable Long id) {
        return filmService.findById(id);
    }

    @PostMapping
    public ResponseEntity<FilmDto> save(@RequestBody FilmCreationDto film) {
        FilmDto nouveauFilm = filmService.save(film);
        URI location = URI.create("/films/" + nouveauFilm.id());
        return ResponseEntity.created(location).body(nouveauFilm);
    }

    @PutMapping("/{id:\\d+}")
    public FilmDto update(@PathVariable Long id, @RequestBody FilmCreationDto film) {
        return filmService.update(id, film);
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        filmService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id:\\d+}/acteurs/{acteurId:\\d+}")
    public FilmDetailDto ajouterActeur(@PathVariable Long id, @PathVariable Long acteurId,
            @RequestBody(required = false) RoleCreationDto role) {
        return filmService.ajouterActeur(id, acteurId, role);
    }
    @DeleteMapping("/{id:\\d+}/acteurs/{acteurId:\\d+}")
    public ResponseEntity<Void> retirerActeur(@PathVariable Long id, @PathVariable Long acteurId) {
        filmService.retirerActeur(id, acteurId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id:\\d+}/acteurs")
    public List<ActeurDto> findActeurs(@PathVariable Long id) {
        return filmService.findActeurs(id);
    }
}