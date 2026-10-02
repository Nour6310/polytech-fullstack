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

import com.exemple.back.dto.ActeurCreationDto;
import com.exemple.back.dto.ActeurDto;
import com.exemple.back.service.ActeurService;

@RestController
@RequestMapping("/acteurs")
public class ActeurController {

    private final ActeurService acteurService;

    public ActeurController(ActeurService acteurService) {
        this.acteurService = acteurService;
    }

    @GetMapping
    public List<ActeurDto> findAll() {
        return acteurService.findAll();
    }

    @GetMapping("/{id:\\d+}")
    public ActeurDto findById(@PathVariable Long id) {
        return acteurService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ActeurDto> save(@RequestBody ActeurCreationDto acteur) {
        ActeurDto nouvelActeur = acteurService.save(acteur);
        URI location = URI.create("/acteurs/" + nouvelActeur.id());
        return ResponseEntity.created(location).body(nouvelActeur);
    }

    @PutMapping("/{id:\\d+}")
    public ActeurDto update(@PathVariable Long id, @RequestBody ActeurCreationDto acteur) {
        return acteurService.update(id, acteur);
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        acteurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}