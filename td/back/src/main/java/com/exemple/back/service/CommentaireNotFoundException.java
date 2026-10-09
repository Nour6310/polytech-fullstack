package com.exemple.back.service;
public class CommentaireNotFoundException extends RuntimeException{
    public CommentaireNotFoundException(long id){
        super("Aucun commentaire trouvé avec l'id "+id);
            }
}
