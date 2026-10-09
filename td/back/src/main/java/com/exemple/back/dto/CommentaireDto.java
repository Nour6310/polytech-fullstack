package com.exemple.back.dto;

import java.time.LocalDateTime;

public record CommentaireDto(
        Long id,
        String auteur,
        LocalDateTime dateCreation,
        String message
) { }
