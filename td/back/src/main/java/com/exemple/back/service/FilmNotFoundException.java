package com.exemple.back.service;
public class FilmNotFoundException extends RuntimeException{
    public FilmNotFoundException(long id){
        super("Aucun film trouvé avec l'id"+id);
            }
}