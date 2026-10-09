package com.exemple.back.dto;

import com.exemple.back.model.Commentaire;

public final class CommentaireMapper {
    public static CommentaireDto toDto(Commentaire commentaire) {
        return new CommentaireDto(
                commentaire.getId(),
                commentaire.getAuteur(),
                commentaire.getDateCreation(),
                commentaire.getMessage()
        );
    }
    public static Commentaire toEntity(CommentaireCreationDto dto) {
        Commentaire commentaire = new Commentaire();
        commentaire.setAuteur(dto.auteur().trim());
        commentaire.setMessage(dto.message().trim());
        return commentaire;
    }
}
