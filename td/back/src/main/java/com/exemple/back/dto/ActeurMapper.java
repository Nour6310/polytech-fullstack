package com.exemple.back.dto;

import com.exemple.back.model.Acteur;

public final class ActeurMapper {
    public static ActeurDto toDto(Acteur acteur) {
        return new ActeurDto(
                acteur.getId(),
                acteur.getPrenom(),
                acteur.getNom()
        );
    }
    public static Acteur toEntity(ActeurCreationDto dto) {
        Acteur acteur = new Acteur();
        acteur.setPrenom(dto.prenom());
        acteur.setNom(dto.nom());
        return acteur;
    }
}