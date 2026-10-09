package com.exemple.back.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.back.dto.CommentaireCreationDto;
import com.exemple.back.dto.CommentaireDto;
import com.exemple.back.service.CommentaireService;

@RestController
public class CommentaireController {

    private final CommentaireService commentaireService;

    public CommentaireController(CommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }

    @GetMapping("/films/{id:\\d+}/commentaires")
    public List<CommentaireDto> findByFilm(@PathVariable Long id) {
        return commentaireService.findByFilm(id);
    }

    @PostMapping("/films/{id:\\d+}/commentaires")
    public ResponseEntity<CommentaireDto> save(@PathVariable Long id,
            @RequestBody(required = false) CommentaireCreationDto commentaire) {
        CommentaireDto nouveauCommentaire = commentaireService.save(id, commentaire);
        URI location = URI.create("/commentaires/" + nouveauCommentaire.id());
        return ResponseEntity.created(location).body(nouveauCommentaire);
    }

    @GetMapping("/commentaires/{id:\\d+}")
    public CommentaireDto findById(@PathVariable Long id) {
        return commentaireService.findById(id);
    }

    @DeleteMapping("/commentaires/{id:\\d+}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentaireService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
