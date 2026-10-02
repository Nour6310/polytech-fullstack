package com.exemple.back.service;

public class ActeurNotFoundException extends RuntimeException {

    public ActeurNotFoundException(Long id) {
        super("Aucun acteur trouvé avec l'id " + id);
    }
}